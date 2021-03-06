package ee.expensetracker.dao;

import ee.expensetracker.model.Goal;
import ee.expensetracker.model.User;
import ee.expensetracker.service.MyUserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Primary
@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private CategoryDao categoryDao;

    @Transactional
    public String save(User user) {
        if (findByName(user.getUsername()).size() == 0) {
            if (user.getUserId() == null) {
                em.persist(user);
            } else {
                em.merge(user);
            }
            User u = findByName(user.getUsername()).get(0);
            categoryDao.createDefaultCategories(u);
            return "";
        } else {
            return "This username already exists!";
        }
    }

    public Long getLoggedInUserId() {
        Long id = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            id = ((MyUserPrincipal) principal).getUserId();
        }
        return id;
    }

    public String isUnique(String user) {
        if(findByName(user).size() == 0){
            return "";
        } else{
            return "This username already exists!";
        }
    }

    public boolean doesUsernameExist (String username) {
       Long count = (Long) em.createQuery(
               "select count(u) from User u where u.username = :username").setParameter("username", username).getSingleResult();

        User u = findUserByUsername(username);
        return (count != (0L));
    }


    public List<User> findAll() {
        return em.createQuery(
                "select u from User u",
                User.class).getResultList();
    }

    public User getUserById(Long id) {
        return em.createQuery(
                "select u from  User u where u.userId = :id",
                User.class).setParameter("id", id).getSingleResult();
    }

    public User findUserByUsername(String username) {
        return em.createQuery(
                "select u from User u where u.username = :username",
                User.class).setParameter("username", username).getSingleResult();
    }

    @Transactional
    public void addGoal(Goal goal) {

        User user = em.find(User.class, getLoggedInUserId());
        goal.setUser(user);
        em.persist(goal);
    }

    public Double getGoal() {
        Long id = em.find(User.class, getLoggedInUserId()).getUserId();
        List<Goal> goals = em.createQuery(
                "select g from Goal g where g.user.userId = :id",
                Goal.class).setParameter("id", id).getResultList();

        System.out.println(goals);
        if (goals.size() == 0){
            return Double.valueOf(0);
        }
        return goals.get(goals.size() - 1).getAmount();
    }

    public List<User> findByName(String name) {
        return em.createQuery(
                "select u from User u where u.username = :name",
                User.class)
                .setParameter("name", name).getResultList();
    }
}
