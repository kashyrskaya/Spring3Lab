package lt.esdc.service;

import lt.esdc.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService implements UserDetailsService {

    private final Map<String, User> memory = new ConcurrentHashMap<>();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!memory.containsKey(username)) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return memory.get(username);
    }

    public void saveUser(User user) {
        if (memory.containsKey(user.getUsername())) {
            throw new RuntimeException("User " + user.getUsername() + " already exists!");
        }
        memory.put(user.getUsername(), user);
    }

    public boolean exists(String username) {
        return memory.containsKey(username);
    }
}