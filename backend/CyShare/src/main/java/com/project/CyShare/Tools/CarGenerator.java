package com.project.CyShare.Tools;

import com.github.javafaker.Faker;
import com.project.CyShare.app.Car;

import java.io.IOException;
import java.util.*;

public class CarGenerator {

    private Map<String, List<String>> brandsAndModels = new HashMap<>();

    private List<String> colorList = new ArrayList<>();
    private String[] colors = {"Red", "Green", "Blue", "Yellow", "Black", "Gray", "White"};

    private final int MAX_YEAR = 2021;
    private final int MIN_YEAR = 2000;

    private String[] acura = {"ILX", "MDX", "NSX", "RDX", "RLX", "TLX"};
    private String[] chevrolet = {"Blazer", "Bolt", "Camaro", "Cruze", "Impala", "Malibu"};
    private String[] dodge = {"Challenger", "Charger", "Durango", "Journey", "Avenger", "Dart"};
    private String[] ford = {"Bronco", "Edge", "Escape", "Explorer", "Focus", "Mustang"};
    private String[] honda = {"Accord", "Civic", "CR-V", "Pilot", "Odyssey", "Clarity"};
    private String[] hyundai = {"Accent", "Elantra", "Ioniq", "Kona", "Santa Fe", "Sonata"};
    private String[] kia = {"Forte", "Stinger", "Optima", "Sportage", "Sorento", "Soul"};
    private String[] mazda = {"3", "6", "CX-3", "CX-30", "CX-5", "CX-9"};
    private String[] subaru = {"Ascent", "BRZ", "Forester", "Impreza", "Outback", "WRX"};
    private String[] toyota = {"4Runner", "Camry", "Corolla", "Prius", "Supra", "Highlander"};

    public CarGenerator() throws IOException
    {
        brandsAndModels.put("Acura", Arrays.asList(acura));
        brandsAndModels.put("Chevrolet", Arrays.asList(chevrolet));
        brandsAndModels.put("Dodge", Arrays.asList(dodge));
        brandsAndModels.put("Ford", Arrays.asList(ford));
        brandsAndModels.put("Honda", Arrays.asList(honda));
        brandsAndModels.put("Hyundai", Arrays.asList(hyundai));
        brandsAndModels.put("Kia", Arrays.asList(kia));
        brandsAndModels.put("Mazda", Arrays.asList(mazda));
        brandsAndModels.put("Subaru", Arrays.asList(subaru));
        brandsAndModels.put("Toyota", Arrays.asList(toyota));
        colorList = Arrays.asList(colors);
    }

    public List<Car> generate(int amount) throws IOException
    {
        List<Car> carList = new ArrayList<>(amount);
        Random random = new Random();
        PasswordGenerator licensePlate = new PasswordGenerator();

        for (int i = 0; i < amount; i++)
        {
            Car car = new Car();
            int carYear = random.nextInt(MAX_YEAR - MIN_YEAR + 1) + MIN_YEAR;
            int carBrand = random.nextInt(10);
            int carModel = random.nextInt(6);
            int carColor = random.nextInt(7);

            car.setYear(carYear);
            car.setColor(colorList.get(carColor));
            car.setLicensePlate(licensePlate.generate(6).toUpperCase());

            switch (carBrand)
            {
                case 0:
                    car.setMake("Acura");
                    car.setModel(brandsAndModels.get("Acura").get(carModel));
                    break;
                case 1:
                    car.setMake("Chevrolet");
                    car.setModel(brandsAndModels.get("Chevrolet").get(carModel));
                    break;
                case 2:
                    car.setMake("Dodge");
                    car.setModel(brandsAndModels.get("Dodge").get(carModel));
                    break;
                case 3:
                    car.setMake("Ford");
                    car.setModel(brandsAndModels.get("Ford").get(carModel));
                    break;
                case 4:
                    car.setMake("Honda");
                    car.setModel(brandsAndModels.get("Honda").get(carModel));
                    break;
                case 5:
                    car.setMake("Hyundai");
                    car.setModel(brandsAndModels.get("Hyundai").get(carModel));
                    break;
                case 6:
                    car.setMake("Kia");
                    car.setModel(brandsAndModels.get("Kia").get(carModel));
                    break;
                case 7:
                    car.setMake("Mazda");
                    car.setModel(brandsAndModels.get("Mazda").get(carModel));
                    break;
                case 8:
                    car.setMake("Subaru");
                    car.setModel(brandsAndModels.get("Subaru").get(carModel));
                    break;
                case 9:
                    car.setMake("Toyota");
                    car.setModel(brandsAndModels.get("Toyota").get(carModel));
                    break;
            }
            carList.add(car);
        }
        return carList;
    }
}
