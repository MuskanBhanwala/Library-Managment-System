package lms.demo.dao;

import lms.demo.model.Member;
import lms.demo.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class MemberDAO {

    public void saveMember(Member member) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(member);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public Member getMemberById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.get(Member.class, id);
        } finally {
            session.close();
        }
    }

    public List<Member> getAllMembers() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery("from Member", Member.class).getResultList();
        } finally {
            session.close();
        }
    }

    public void updateMember(Member member) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(member);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void deleteMember(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Member member = session.get(Member.class, id);
            if (member != null) {
                session.delete(member);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
