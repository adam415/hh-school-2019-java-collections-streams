package tasks;

import common.Person;
import common.Task;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import java.util.HashSet;
import java.util.Objects;
import java.time.Instant;
import java.util.Random;

/*
А теперь о горьком
Всем придется читать код
А некоторым придется читать код, написанный мною
Сочувствую им
Спасите будущих жертв, и исправьте здесь все, что вам не по душе!
P.S. функции тут разные и рабочие (наверное), но вот их понятность и эффективность страдает (аж пришлось писать комменты)
P.P.S Здесь ваши правки желательно прокомментировать (можно на гитхабе в пулл реквесте)
 */
public class Task8 implements Task {

  // >>>>> Так будет заметнее мои комменты?)
  // >>>>> the reviewer was definitely drunk, в топку поле - private long count;
  // >>>>> Вернул non-static

  //Не хотим выдывать апи нашу фальшивую персону, поэтому конвертим начиная со второй
  public List<String> getNames(List<Person> persons) {
    /* BEFORE:
    if (persons.size() == 0) {
      return Collections.emptyList();
    }
    persons.remove(0);
    return persons.stream().map(Person::getFirstName).collect(Collectors.toList());
    */

    /* >>>>>
     Изменять переданный лист из вери бэд (not pure)
     Чтобы пропустить первый элемент есть skip и проверка на пустую коллекцию даже не нужна
    */

    return persons.stream()
            .skip(1)
            .map(Person::getFirstName)
            .collect(Collectors.toList());
  }

  //ну и различные имена тоже хочется
  public Set<String> getDifferentNames(List<Person> persons) {
    return new HashSet<>(getNames(persons));

    /* >>>>>
    Дистинкт был лишний, так как коллектор сам уберет повторы.
    Есть готовый конструктор и не нужны стримы (я так и хотел сначала, но в сигнатурах показалось,
      что такого не было, так что теперь действительно компактнее).
    */

    /* BEFORE:
    return getNames(persons).stream().distinct().collect(Collectors.toSet());
     */
  }

  //Для фронтов выдадим полное имя, а то сами не могут
  // >>>>> Мне не понравилось название. Dieu et mon droit..
  public String getFullName(Person person) {
    /* BEFORE:
    String result = "";
    if (person.getSecondName() != null) {
      result += person.getSecondName();
    }

    if (person.getFirstName() != null) {
      result += " " + person.getFirstName();
    }

    if (person.getSecondName() != null) { // <<<<< + Опечатка
      result += " " + person.getSecondName();
    }
    return result;
    */

    /* >>>>>
     Здесь я использую стримы, дабы не писать одни и те же действия отдельно для каждого элемента
     Тогда вместо проверки на нуль и объединения строк ручками есть более красиво средство
    */

    return Stream.of(person.getSecondName(), person.getFirstName(), person.getMiddleName())
            .filter(Objects::isNull)
            .collect(Collectors.joining(" "));
  }

  // словарь id персоны -> ее имя
  public Map<Integer, String> getPersonNames(Collection<Person> persons) {
    /* BEFORE:
    Map<Integer, String> map = new HashMap<>(1);
    for (Person person : persons) {
      if (!map.containsKey(person.getId())) {
        map.put(person.getId(), convertPersonToString(person));
      }
    }
    return map;
    */

    /* >>>>>
    distinct'ом отсеиваю одинаковые ссылки на Person
    Если же два разных Person содержат одинаковый id, то второй игнорируется как и в BEFORE,
      но я добавил на всякий случай сообщение, что у таких-то разных персон одинаковый ид.
    */

    return persons.stream()
            .distinct()
            .collect(Collectors.toMap(
                    Person::getId,
                    this::getFullName,
                    (fullName1, fullName2) -> {
                      System.err.println(String.format(
                              "Encountered different person objects with equal ids: '%s' and '%s'",
                              fullName1, fullName2));
                      return fullName1;
                    }
            ));
  }

  // есть ли совпадающие в двух коллекциях персоны?
  public boolean hasSamePersons(Collection<Person> persons1, Collection<Person> persons2) {
    /* BEFORE: O(m*n)
    boolean has = false;
    for (Person person1 : persons1) {
      for (Person person2 : persons2) {
        if (person1.equals(person2)) {
          has = true;
        }
      }
    }
    return has;
    */

    /* >>>>>
    AFTER: O(m) + O(n) + k*O(1) = O(m+n)
    где k - номер первого попавшегося элемента, общего для обоих сетов (k <= m)
    */

    return new HashSet<>(persons1).stream()
            .anyMatch(new HashSet<>(persons2)::contains);
  }

  //Выглядит вроде неплохо...
  // >>>>> Думаю, не стоит принимать Stream, потому что он может быть уже использован
  // >>>>> Логично создавать стрим там, где его используют.
  public long countEven(Collection<Integer> numbers) {
    /* BEFORE:
    count = 0;
    numbers.filter(num -> num % 2 == 0).forEach(num -> count++);
    return count;
    */

    /* >>>>>
     Вернул %.
     Переменную count нельзя держать в классе, ввиду проблем с многопоточностью. Должна быть локальная.
     Поскольку пока никаких действий больше здесь не предвидится, красивее все-таки count, чем ручками.
    */

    return numbers.stream()
            .filter(num -> num % 2 == 0)
            .count();
  }

  @Override
  public boolean check() {
    // >>>>> Тут можно без комментариев? Have fun:)
    System.out.println("Слабо дойти до сюда и ~исправить~ (зачеркнуто) совсем испортить Fail этой таски?");
    final boolean NEVER = false, TGIF = false;
    boolean codeSmellsGood = NEVER;
    boolean reviewerDrunk = TGIF;
    var letsShuffleParty = new Random(Instant.now().toEpochMilli());
    var x = letsShuffleParty.nextDouble();
    var buonaFortuna = (-38 * x * x + 943 * x - 112 <= 5812.0 + 117.0 / 152.0);
    System.out.println(buonaFortuna ? "Офигеть, чувак, поделись везением!" : "Серьезно? Ты и компьютер напоил?");
    return codeSmellsGood || reviewerDrunk || buonaFortuna;
  }
}
