package beforesuper;

public class Client extends Person{

    public Client(String name, int age) {
        if(age >= 18)
            throw new IllegalArgumentException("BAD AGE!");

        //super(name, age);
    }

}
