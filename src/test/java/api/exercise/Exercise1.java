package api.exercise;

import lambda.data.Person;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@SuppressWarnings({"ConstantConditions", "unused", "MismatchedQueryAndUpdateOfCollection"})
public class Exercise1 {

    enum Status {
        UNKNOWN,
        PENDING,
        COMMENTED,
        ACCEPTED,
        DECLINED
    }

    @Test
    public void acceptGreaterThan21OthersDecline() {
        Person alex = new Person("Алексей", "Мельников", 20);
        Person ivan = new Person("Иван", "Стрельцов", 24);
        Person helen = new Person("Елена", "Рощина", 22);
        Map<Person, Status> candidates = new HashMap<>();
        candidates.put(alex, Status.PENDING);
        candidates.put(ivan, Status.PENDING);
        candidates.put(helen, Status.PENDING);

        candidates = candidates.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> e.getKey().getAge() > 21 ? Status.ACCEPTED : Status.DECLINED
        ));

        assertEquals(Status.ACCEPTED, candidates.get(ivan));
        assertEquals(Status.ACCEPTED, candidates.get(helen));
        assertEquals(Status.DECLINED, candidates.get(alex));
    }

    @Test
    public void acceptGreaterThan21OthersRemove() {
        Person alex = new Person("Алексей", "Мельников", 20);
        Person ivan = new Person("Иван", "Стрельцов", 24);
        Person helen = new Person("Елена", "Рощина", 22);
        Map<Person, Status> candidates = new HashMap<>();
        candidates.put(alex, Status.PENDING);
        candidates.put(ivan, Status.PENDING);
        candidates.put(helen, Status.PENDING);

        candidates = candidates.entrySet().stream().filter(e -> e.getKey().getAge() > 21)
                .collect(Collectors.toMap(
                        e -> e.getKey(),
                        e -> Status.ACCEPTED
                ));

        Map<Person, Status> expected = new HashMap<>();
        expected.put(ivan, Status.ACCEPTED);
        expected.put(helen, Status.ACCEPTED);
        assertEquals(expected, candidates);
    }

    @Test
    public void getStatus() {
        Person alex = new Person("Алексей", "Мельников", 20);
        Person ivan = new Person("Иван", "Стрельцов", 24);
        Person helen = new Person("Елена", "Рощина", 22);
        Map<Person, Status> candidates = new HashMap<>();
        candidates.put(alex, Status.PENDING);
        candidates.put(ivan, Status.PENDING);

        BiFunction<Map<Person, Status>, Person, Status> statusExtractor = (map, key) -> {
            Status status = map.get(key);
            return Objects.nonNull(map.get(key)) ? status : Status.UNKNOWN;
        };

        Status alexStatus = statusExtractor.apply(candidates, alex);
        Status ivanStatus = statusExtractor.apply(candidates, ivan);
        Status helenStatus = statusExtractor.apply(candidates, helen);

        assertEquals(Status.PENDING, alexStatus);
        assertEquals(Status.PENDING, ivanStatus);
        assertEquals(Status.UNKNOWN, helenStatus);
    }

    @Test
    public void putToNewValuesIfNotExists() {
        Person alex = new Person("Алексей", "Мельников", 20);
        Person ivan = new Person("Иван", "Стрельцов", 24);
        Person helen = new Person("Елена", "Рощина", 22);
        Person dmitry = new Person("Дмитрий", "Егоров", 30);
        Map<Person, Status> oldValues = new HashMap<>();
        oldValues.put(alex, Status.PENDING);
        oldValues.put(dmitry, Status.DECLINED);
        oldValues.put(ivan, Status.ACCEPTED);

        Map<Person, Status> newValues = new HashMap<>();
        newValues.put(alex, Status.DECLINED);
        newValues.put(helen, Status.PENDING);

        oldValues.entrySet().stream()
                .filter(personStatusEntry -> !newValues.containsKey(personStatusEntry.getKey()))
                .forEach(personStatusEntry -> newValues.put(personStatusEntry.getKey(), personStatusEntry.getValue()));

        assertEquals(Status.DECLINED, newValues.get(alex));
        assertEquals(Status.ACCEPTED, newValues.get(ivan));
        assertEquals(Status.PENDING, newValues.get(helen));
    }
}
