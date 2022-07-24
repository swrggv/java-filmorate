package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ModelNotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventOperations;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.EventsStorage;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.impl.FriendDbStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;
    private final FriendStorage friendStorage;
    private final EventsStorage eventsStorage;

    @Autowired
    public UserService(UserStorage userStorage, FriendDbStorage friendStorage, EventsStorage eventsStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
        this.eventsStorage = eventsStorage;
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User changeUser(User user) {
        return userStorage.changeUSer(user);
    }

    public void addFriendToUser(long userId, long friendId) {
        User user = getUserById(userId);
        User friendUser = getUserById(friendId);
        Collection<User> users = getAllUsers();
        if (!users.contains(user) && users.contains(friendUser)) {
            throw new ModelNotFoundException("User not found");
        } else {
            friendStorage.addFriendToUser(userId, friendId);
            eventsStorage.addEvent(userId, friendId, EventType.FRIEND, EventOperations.ADD);
        }
    }

    public void deleteFromFriend(long userId, long friendId) {
        friendStorage.deleteFromFriends(userId, friendId);
        eventsStorage.addEvent(userId, friendId, EventType.FRIEND, EventOperations.REMOVE);
    }

    public List<User> getUserFriends(long id) {
        //user existence check
        userStorage.findUserById(id);
        List<User> userFriends = new ArrayList<>();
        List<Long> friendsId = friendStorage.getUserFriends(id);
        for (Long oneId : friendsId) {
            userFriends.add(getUserById(oneId));
        }
        return userFriends;
    }

    public List<User> getCommonFriend(long id, long otherId) {
        List<User> user = getUserFriends(id);
        List<User> anotherUser = getUserFriends(otherId);
        return user.stream().filter(anotherUser::contains).collect(Collectors.toList());
    }

    public User getUserById(long id) {
        return userStorage.findUserById(id);
    }

    public User checkName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return user;
    }

    public void deleteUser(long id) {
        getUserById(id);
        userStorage.deleteUser(id);
    }

    public List<Event> getEvents(Long id) {
        getUserById(id);
        return eventsStorage.getEvents(id);
    }
}