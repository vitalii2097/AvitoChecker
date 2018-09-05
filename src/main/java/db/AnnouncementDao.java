package db;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class AnnouncementDao {

    public void save(DbAnnouncement announcement) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(announcement);
        tx1.commit();
        session.close();
    }


    public void update(DbAnnouncement announcement) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(announcement);
        tx1.commit();
        session.close();
    }


    public void delete(DbAnnouncement announcement) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(announcement);
        tx1.commit();
        session.close();
    }

}
