package gatherers;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Gatherer;
import java.util.stream.Gatherers;
import java.util.stream.Stream;

public class App {

    public static void main(String[] args) {

        //Stream.of(1,2,3,4,5,6,7,8,9).gather(Gatherers.windowFixed(3)).forEach(System.out::println);
        //Stream.of(1,2,3,4,5,6,7,8,9).gather(Gatherers.windowSliding(3)).forEach(System.out::println);

        /*Optional<String> numbersToString = Stream.of(20, 30, 40, 50)
                .gather(Gatherers.fold(() -> "", (string, number) -> string + number)).findFirst();

        numbersToString.ifPresent(System.out::println);*/

        List<String> list = Stream.of(
                        new Person("Juan", 20),
                        new Person("Maria", 25),
                        new Person("Pedro", 30),
                        new Person("Luis", 35),
                        new Person("Ana", 40),
                        new Person("Elena", 45)
                )
                //.filter(person -> person.getAge() % 3 == 0)
                .gather(customFilter(person -> person.getAge() % 3 == 0))
                //.map(Person::getName)
                .gather(customMap(Person::getName))
                .toList();

        list.forEach(System.out::println);
    }

    public static Gatherer<Person, ?, Person> customFilter(Predicate<? super Person> filter){
        Gatherer.Integrator<Void, Person, Person> integrator = (state, element, downstream) -> {
            boolean isOk = filter.test(element);
            if(isOk){
                downstream.push(element);
            }else{
                downstream.push(new Person("DEFAULT", 99));
            }
            return true;
        };
        return Gatherer.of(integrator);
    }

    public static Gatherer<Person, ?, String> customMap(Function<? super Person, ? extends String> mapper){
        Gatherer.Integrator<Void, Person, String> integrator = (_, element, downstream) -> {
          if(element.getAge() > 25){
              String newElement = mapper.apply(element);
              downstream.push(newElement);
          }else{
              downstream.push("NO PASS");
          }
          return true;
        };
        return Gatherer.of(integrator);
    }
}
