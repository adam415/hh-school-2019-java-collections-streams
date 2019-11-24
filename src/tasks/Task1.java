package tasks;

import common.Person;
import common.PersonService;
import common.Task;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import java.util.function.Function;
import java.util.Map;

/*
Задача 1
Метод на входе принимает List<Integer> id людей, ходит за ними в сервис (он выдает несортированный Set<Person>)
нужно их отсортировать в том же порядке, что и переданные id.
Оценить асимпотику работы
 */
public class Task1 implements Task {

  // !!! Редактируйте этот метод !!!
  private List<Person> findOrderedPersons(List<Integer> personIds) {
    Set<Person> foundPersons = PersonService.findPersons(personIds);

    Map<Integer, Person> collocatedPersons = foundPersons.stream()
            .collect(Collectors.toUnmodifiableMap(Person::getId, Function.identity()));

    List<Person> orderedPersons = personIds.stream()
            .map(collocatedPersons::get)
            .collect(Collectors.toList());

    return orderedPersons;
  }

  @Override
  public boolean check() {
    List<Integer> ids = List.of(1, 2, 3);

    return findOrderedPersons(ids).stream()
        .map(Person::getId)
        .collect(Collectors.toList())
        .equals(ids);
  }

}
