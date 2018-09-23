package com.hibernate.n1;

import com.hibernate.n1.config.HibernateUtils;
import com.hibernate.n1.entity.Account;
import com.hibernate.n1.entity.Person;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class Main {

    private static Session session = HibernateUtils.getSession();


    public static void prepareDB(){
        Person person = new Person();
        person.setName("Jan");
        person.setLastname("Kowalski");

        Account account = new Account();
        account.setTitle("Konto");
        account.setPerson(person);

        Transaction transaction = session.beginTransaction();
        session.save(account);
        //dodaje konto razem z osobą, obiekt person po dodaniu bedzie miał przypisane ID
        //nie chce tworzyć nowej osoby, tylko jedną przypisać do wielu rekordów
        transaction.commit();
        System.out.println("Id osoby" + account.getPerson().getId());

        Account account1 = new Account();
        account1.setTitle("mBank");
        //pobieram osobę, która już istnieje w bazie
        account1.setPerson(session.get(Person.class, account.getPerson().getId()));

        Account account2 = new Account();
        account2.setTitle("AliorBank");
        //pobieram osobę, która już istnieje w bazie
        account2.setPerson(session.get(Person.class, account.getPerson().getId()));

        transaction.begin();
        session.save(account1);
        session.save(account2);
        transaction.commit();
        session.close();
    }

    public static void main(String[] args) {

        List<Person> personList = session.createQuery("FROM Person p").list();

        for (Person person : personList) {
            System.out.println("Osoba: " + person.getName() + " : "
                    + person.getLastname());
            System.out.println("----- Banki -----");
            for (Account account : person.getAccountSet()) {
                System.out.println("Nazwa banku: " + account.getTitle());
            }
        }


        HibernateUtils.closeConnection();
    }
}
