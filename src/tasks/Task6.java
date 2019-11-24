package tasks;

import common.Area;
import common.Person;
import common.Task;

import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.function.Function;
import java.util.stream.Collectors;

/*
Имеются
- коллекция персон Collection<Person>
- словарь Map<Integer, Set<Integer>>, сопоставляющий каждой персоне множество id регионов
- коллекция всех регионов Collection<Area>
На выходе хочется получить множество строк вида "Имя - регион". Если у персон регионов несколько, таких строк так же будет несколько
 */
public class Task6 implements Task {

  private Set<String> getPersonDescriptions(Collection<Person> persons,
                                            Map<Integer, Set<Integer>> personAreaIds,
                                            Collection<Area> areas) {
    // Делаю без проверок, чтобы выглядело красивее
    Map<Integer, String> namesMap = persons.stream()
            .collect(Collectors.toUnmodifiableMap(
                    Person::getId,
                    Person::getFirstName
            ));

    Function<Integer, String> getAreaName = areaId -> areas.stream()
            .filter(area -> area.getId().equals(areaId))
            .findAny().get().getName();

    return personAreaIds.entrySet().stream()
            .flatMap(pers_areas -> pers_areas.getValue().stream()
                    .map(areaId -> Map.entry(
                            pers_areas.getKey(),
                            getAreaName.apply(areaId)
                    )))
            .map(pers_area -> String.format("%s - %s",
                    namesMap.get(pers_area.getKey()),
                    pers_area.getValue()))
            .collect(Collectors.toSet());
  }

  @Override
  public boolean check() {
    List<Person> persons = List.of(
        new Person(1, "Oleg", Instant.now()),
        new Person(2, "Vasya", Instant.now())
    );
    Map<Integer, Set<Integer>> personAreaIds = Map.of(1, Set.of(1, 2), 2, Set.of(2, 3));
    List<Area> areas = List.of(new Area(1, "Moscow"), new Area(2, "Spb"), new Area(3, "Ivanovo"));
    return getPersonDescriptions(persons, personAreaIds, areas)
        .equals(Set.of("Oleg - Moscow", "Oleg - Spb", "Vasya - Spb", "Vasya - Ivanovo"));
  }
}
