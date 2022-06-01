package com.company;

import com.company.Exceptions.AutoException;
import com.company.Exceptions.ClientException;
import com.company.Exceptions.UserException;
import com.company.client.Client;
import com.company.client.ClientService;
import com.company.users.Role;
import com.company.users.User;
import com.company.users.UserService;

import javax.persistence.NoResultException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import static com.company.users.Role.*;

public class Main {

    private static final String BLOGA_IVESTIS = "Neatpazinta ivestis";
    private static final Scanner SC = new Scanner(System.in);
    public static final double FEE_INDEX = 1.8;

    public static void main(String[] args) {

        UserService userService = new UserService();
        ClientService clientService = new ClientService();


            while (true) {
                printLoginMenu();
                String loginInput = SC.nextLine();
                switch (loginInput) {
                    case "1":
                        System.out.print("Iveskite prisijungimo varda: ");
                        String username = SC.nextLine();
                        System.out.print("Iveskite slaptazodi: ");
                        String password = SC.nextLine();
                        try {
                            User loginUser = userService.getUser(username, password);
                            System.out.println("Prisijungimas sekmingas");
                            System.out.println();
                            userMenu(loginUser, userService, clientService);
                        } catch (NoResultException e) {
                            System.out.println("Tokio vartotojo nera arba blogas slaptazodis");
                        }
                        break;
                    case "2":
                        registerNewUser(userService, clientService);
                        break;
                    case "3":
                        return;
                    default:
                        System.out.println(BLOGA_IVESTIS);
                        break;
                }
                System.out.println();
            }
    }

    private static void userMenu(User user, UserService userService, ClientService clientService) {

        if (user.getRole().equals(REGISTER)) registerMenu(user, userService, clientService);
        if (user.getRole().equals(MECHANIC)) mechanicMenu(user, userService, clientService);
        if (user.getRole().equals(ADMIN)) adminMenu(user, userService, clientService);
    }

    private static void registerMenu(User user, UserService userService, ClientService clientService) {

        while (true) {
            printRegisterMenu();
            String choice = SC.nextLine();
            switch (choice) {
                case "1":
                    addClientAuto(clientService);
                    break;
                case "2":
                    try {
                        List<Client> fixedAutos = clientService.getAllAutos(true);
                        printClientsAuto(fixedAutos);
                    } catch (AutoException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "3":
                    searchAutoByClientName(clientService);
                    break;
                case "4":
                    returnAuto(clientService);
                    break;
                case "5":
                    printUserInfo(user);
                    break;
                case "6":
                    changePassword(user, userService);
                    break;
                case "7":
                    return;
                default:
                    System.out.println(BLOGA_IVESTIS);
                    break;
            }
            System.out.println();
        }
    }

    private static void mechanicMenu(User user, UserService userService, ClientService clientService) {

        while (true) {
            printMechanicMenu();
            String choice = SC.nextLine();
            switch (choice) {
                case "1":
                    markAutoFixed(clientService);
                    break;
                case "2":
                    try {
                        List<Client> notFixedAutos = clientService.getAllAutos(false);
                        printClientsAuto(notFixedAutos);
                    } catch (AutoException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "3":
                    printUserInfo(user);
                    break;
                case "4":
                    changePassword(user, userService);
                    break;
                case "5":
                    return;
                default:
                    System.out.println(BLOGA_IVESTIS);
                    break;
            }
            System.out.println();
        }
    }

    private static void adminMenu(User adminUser, UserService userService, ClientService clientService) {

        while (true) {
            printAdminUserMenu();
            String choice = SC.nextLine();
            switch (choice) {
                case "1":
                    List<User> allUsers = userService.getAllUsers();
                    printUsersInfo(allUsers);
                    break;
                case "2":
                    addNewUser(userService);
                    break;
                case "3":
                    printDeletion(adminUser, userService);
                    break;
                case "4":
                    printBudget(clientService);
                    break;
                case "5":
                    changePassword(adminUser, userService);
                    break;
                case "6":
                    return;
                default:
                    System.out.println(BLOGA_IVESTIS);
                    break;
            }
            System.out.println();
        }
    }

    private static void printLoginMenu() {

        System.out.println("[1] Prisijungimas");
        System.out.println("[2] Registracija");
        System.out.println("[3] EXIT");
    }

    private static void printRegisterMenu() {

        System.out.println("[1] Registruoti automobili remontui");
        System.out.println("[2] Rodyti visus sutvarkytus automobilius");
        System.out.println("[3] Ieskoti automobili pagal kliento varda ir pavarde");
        System.out.println("[4] Grazinti automobili klientui");
        System.out.println("[5] Perziureti savo informacija");
        System.out.println("[6] Keisti savo slaptazodi");
        System.out.println("[7] Atsijungti");
    }

    private static void printMechanicMenu() {

        System.out.println("[1] Grazinti automobili po remonto");
        System.out.println("[2] Rodyti visus nesutvarkytus automobilius");
        System.out.println("[3] Perziureti savo informacija");
        System.out.println("[4] Keisti savo slaptazodi");
        System.out.println("[5] Atsijungti");
    }

    private static void printAdminUserMenu() {

        System.out.println("[1] Perziureti visu vartotoju informacija");
        System.out.println("[2] Prideti nauja darbuotoja");
        System.out.println("[3] Istrinti egzistuojanti vartotoja");
        System.out.println("[4] Biudzetas");
        System.out.println("[5] Keisti savo slaptazodi");
        System.out.println("[6] Atsijungti");
    }


    private static void deleteUser(User adminUser, UserService userService, String usernameToDelete) throws UserException {

        if (!adminUser.getUsername().equalsIgnoreCase(usernameToDelete)) {
            userService.deleteUserByUsername(usernameToDelete);
            return;
        }
        throw new UserException("Admin vartotojo istrinti negalima");
    }

    private static void addNewUser(UserService userService) {

        System.out.print("Iveskite prisijungimo varda: ");
        String username = SC.nextLine();
        System.out.print("Iveskite slaptazodi: ");
        String password = SC.nextLine();
        System.out.print("Iveskite varda: ");
        String name = SC.nextLine();
        System.out.print("Iveskite pavarde: ");
        String surname = SC.nextLine();
        int age = getAge();
        Role role = getRoleForNewUser();
        try {
            userService.addUser(new User(username, password, role, name, surname, age));
            System.out.println("Vartotojas sekmingai uzregistruotas");
        } catch (UserException e) {
            System.out.println(e.getMessage());
        }

    }

    private static void registerNewUser(UserService userService, ClientService clientService) {

        System.out.print("Iveskite prisijungimo varda: ");
        String username = SC.nextLine();
        System.out.print("Iveskite slaptazodi: ");
        String password = SC.nextLine();
        System.out.print("Iveskite varda: ");
        String name = SC.nextLine();
        System.out.print("Iveskite pavarde: ");
        String surname = SC.nextLine();
        int age = getAge();
        User user = new User(username, password, name, surname, age);
        try {
            userService.addUser(user);
        } catch (UserException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Vartotojas sekmingai uzregistruotas");
        System.out.println();
        registerMenu(user, userService, clientService);
    }

    private static int getAge() {

        while (true) {
            try {
                System.out.print("Iveskite amziu: ");
                int age = SC.nextInt();
                if (age <= 99 && age >= 1) {
                    SC.nextLine();
                    return age;
                }
                throw new InputMismatchException();
            } catch (InputMismatchException e) {
                System.out.println("Amzius turi buti skaicius 1 - 99");
                SC.nextLine();
            }
        }
    }

    private static Role getRoleForNewUser() {

        System.out.println("Pasirinkite vartotojo lygi:");
        while (true) {
            System.out.println("[1] Register");
            System.out.println("[2] Mechanic");
            String choice = SC.nextLine();
            switch (choice) {
                case "1":
                    return REGISTER;
                case "2":
                    return MECHANIC;
                default:
                    System.out.println(BLOGA_IVESTIS);
            }
        }
    }


    private static void changePassword(User user, UserService userService) {

        System.out.print("Iveskite sena slaptazodi: ");
        String oldPass = SC.nextLine();
        if (!user.getPassword().equals(oldPass)) {
            System.out.println("Neteisingas slaptazodis");
            return;
        }
        while (true) {
            System.out.print("Iveskite nauja slaptazodi: ");
            String newPass = SC.nextLine();
            System.out.print("Pakartokite nauja slaptazodi: ");
            String newPassDuplicate = SC.nextLine();
            if (newPass.equals(newPassDuplicate)) {
                userService.changeUsersPassword(user, newPass);
                System.out.println("Slaptazodis sekmingai pakeistas");
                return;
            }
            System.out.println("Slaptazodziai nesutampa");
            System.out.println();
        }

    }

    private static void printUsersInfo(List<User> users) {

        for (User user : users) {
            System.out.println("--------------------------------------");
            System.out.println("Prisijungimo vardas: " + user.getUsername());
            System.out.println("Vardas: " + user.getName());
            System.out.println("Pavarde: " + user.getSurname());
            System.out.println("Amzius: " + user.getAge());
            System.out.println("Role: " + user.getRole());
        }
    }

    private static void printUserInfo(User user) {
        System.out.println("--------------------------------------");
        System.out.println("Prisijungimo vardas: " + user.getUsername());
        System.out.println("Vardas: " + user.getName());
        System.out.println("Pavarde: " + user.getSurname());
        System.out.println("Amzius: " + user.getAge());
        System.out.println("--------------------------------------");
    }

    private static void printClientsAuto(List<Client> clients) {

        for (Client client : clients) {
            System.out.printf("Klientas: %s %s\n", client.getName(), client.getSurname());
            System.out.printf("Automobilis: %s\n", client.getAutoName());
            System.out.printf("Valstybinis numeris: %s\n", client.getPlateNr());
            if (client.isFixed()) {
                System.out.printf("Moketina suma: %.2f\n", client.getCosts() * FEE_INDEX);
            } else {
                System.out.printf("Paslauga/gedimas: %s\n", client.getIssue());
            }
            System.out.println("---------------------------------");
        }
    }

    private static void searchAutoByClientName(ClientService clientService) {

        System.out.print("Iveskite kliento varda: ");
        String clientName = SC.nextLine();
        System.out.print("Iveskite kliento pavarde: ");
        String clientSurname = SC.nextLine();
        System.out.println("-------------------------");
        try {
            List<Client> clientAutos = clientService.getAllAutosForClient(clientName, clientSurname);
            printClientsAuto(clientAutos);
        } catch (ClientException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void returnAuto(ClientService clientService) {

        System.out.print("Iveskite automobilio valstybini numeri: ");
        String autoPlateNrToReturn = SC.nextLine();
        try {
            Client autoToReturn = clientService.getAutoToReturn(autoPlateNrToReturn);
            System.out.printf("Moketina suma: %.2f\n", autoToReturn.getCosts() * FEE_INDEX);
        } catch (AutoException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void addClientAuto(ClientService clientService) {

        System.out.print("Iveskite kliento varda: ");
        String clientName = SC.nextLine();
        System.out.print("Iveskite kliento pavarde: ");
        String clientSurname = SC.nextLine();
        System.out.print("Iveskite automobilio marke ir modeli: ");
        String autoName = SC.nextLine();
        System.out.print("Iveskite automobilio valstybinius numerius: ");
        String plateNr = SC.nextLine();
        System.out.print("Iveskite gedima arba norima paslauga: ");
        String issue = SC.nextLine();
        try {
            clientService.addClient(new Client(clientName, clientSurname, autoName, plateNr, issue));
            System.out.println("Automobilis remontui uzregistruotas");
        } catch (AutoException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void markAutoFixed(ClientService clientService) {

        System.out.print("Iveskite automobilio valstybini numeri: ");
        String fixedAutoPlateNr = SC.nextLine();
        System.out.print("Iveskite remonto kastus: ");
        double expenses = SC.nextDouble();
        SC.nextLine();
        try {
            clientService.markAutoAsFixed(fixedAutoPlateNr, expenses);
            System.out.println("Automobilio statusas sekmingai pakeistas");
        } catch (ClientException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void printDeletion(User adminUser, UserService userService) {

        System.out.print("Iveskite trinamo vartotojo prisijungimo varda: ");
        String usernameToDelete = SC.nextLine();
        try {
            deleteUser(adminUser, userService, usernameToDelete);
            System.out.printf("Vartotojas '%s' sekmingai istrintas\n", usernameToDelete);
        } catch (UserException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void printBudget(ClientService clientService) {

        try {
            double autoServiceExpenses = clientService.getExpenses();
            double autoServiceIncome = clientService.getIncome();
            System.out.printf("Islaidos: %.2f Eur\n", autoServiceExpenses);
            System.out.printf("Pajamos: %.2f Eur\n", autoServiceIncome);
            System.out.printf("Pelnas: %.2f Eur\n", autoServiceIncome - autoServiceExpenses);
        } catch (NullPointerException e) {
            System.out.println("Biudzete truksta duomenu");
        }

    }

}