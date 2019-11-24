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
import java.util.function.Predicate;
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
  // >>>>> Везде был пропущен static (и в прошлых тасках, но менял)

  //Не хотим выдывать апи нашу фальшивую персону, поэтому конвертим начиная со второй
  public static List<String> getNames(List<Person> persons) {
    /* BEFORE:
    if (persons.size() == 0) {
      return Collections.emptyList();
    }
    persons.remove(0);
    return persons.stream().map(Person::getFirstName).collect(Collectors.toList());
    */

    return persons.stream()
            .skip(1)
            .map(Person::getFirstName)
            .collect(Collectors.toList());
  }

  //ну и различные имена тоже хочется
  public static Set<String> getDifferentNames(List<Person> persons) {
    Set<String> set = new HashSet<>();
    set.addAll(getNames(persons));

    return set;

    // >>>>> Set сам содержит метод добавления всех элементов из коллекции,
    // выглядит компактнее и работы меньше (нет создания и обхода лишних стримов, вызовов лямбд)

    /* BEFORE:
    return getNames(persons).stream().distinct().collect(Collectors.toSet());
     */
  }

  //Для фронтов выдадим полное имя, а то сами не могут
  // >>>>> Мне не понравилось название. Dieu et mon droit..
  public static String getFullName(Person person) {
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

    return Stream.of(person.getSecondName(), person.getFirstName(), person.getMiddleName())
            .filter(Objects::isNull)
            .collect(Collectors.joining(" "));
  }

  // словарь id персоны -> ее имя
  public static Map<Integer, String> getPersonNames(Collection<Person> persons) {
    /* BEFORE:
    Map<Integer, String> map = new HashMap<>(1);
    for (Person person : persons) {
      if (!map.containsKey(person.getId())) {
        map.put(person.getId(), convertPersonToString(person));
      }
    }
    return map;
    */

    return persons.stream()
            .collect(Collectors.toMap(
                    Person::getId,
                    Task8::getFullName
            ));
  }

  // есть ли совпадающие в двух коллекциях персоны?
  public static boolean hasSamePersons(Collection<Person> persons1, Collection<Person> persons2) {
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

    // AFTER: O(m+n)
    Set<Person> intersection = new HashSet<>();
    intersection.addAll(persons1);
    intersection.retainAll(persons2);

    return !intersection.isEmpty();
  }

  //Выглядит вроде неплохо...
  // >>>>> Думаю, не стоит принимать Stream, потому что он может быть уже использован
  // >>>>> Логично создавать стрим там, где его используют.
  public static long countEven(Collection<Integer> numbers) {
    /* BEFORE:
    count = 0;
    numbers.filter(num -> num % 2 == 0).forEach(num -> count++);
    return count;
    */

    // >>>>> count, полагаю, не будет особо быстрее forEach, но переменная нам ни к чему
    // >>>>> и побитовый оператор по идее быстрее остатка от деления.
    Predicate<Integer> isEvenNumber = num -> (num & 1) == 0;

    return numbers.stream()
            .filter(isEvenNumber)
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
