package me.committee.api.managers;

import me.committee.api.player.PlayerEnemy;
import me.committee.api.player.PlayerFriend;

import java.util.ArrayList;

public class PlayerManager {

    private final ArrayList<PlayerFriend> friends = new ArrayList<>();
    private final ArrayList<PlayerEnemy> enemies = new ArrayList<>();

    public void init() {

    }

    public boolean addFriend(PlayerFriend friend) {
        if (this.enemies.stream().anyMatch(enemy -> enemy.getName().equalsIgnoreCase(friend.getName())))
            return false;
        if (this.friends.stream().anyMatch(f -> f.getName().equalsIgnoreCase(friend.getName())))
            return false;

        return this.friends.add(friend);
    }

    public boolean addEnemy(PlayerEnemy enemy) {
        if (this.friends.stream().anyMatch(friend -> friend.getName().equalsIgnoreCase(enemy.getName())))
            return false;
        if (this.enemies.stream().anyMatch(e -> e.getName().equalsIgnoreCase(enemy.getName())))
            return false;

        return this.enemies.add(enemy);
    }

    public boolean removeFriendByName(String name) {
        for (PlayerFriend friend : this.friends) {
            if (friend.getName().equalsIgnoreCase(name)) {
                this.friends.remove(friend);
                return true;
            }
        }
        return false;
    }

    public boolean removeEnemyByName(String name) {
        for (PlayerEnemy enemy : this.enemies) {
            if (enemy.getName().equalsIgnoreCase(name)) {
                this.enemies.remove(enemy);
                return true;
            }
        }
        return false;
    }

    public boolean isFriend(String name) {
        for (PlayerFriend friend : this.friends) {
            if (friend.getName().equalsIgnoreCase(name))
                return true;
        }
        return false;
    }

    public boolean isEnemy(String name) {
        for (PlayerEnemy enemy : this.enemies) {
            if (enemy.getName().equalsIgnoreCase(name))
                return true;
        }
        return false;
    }

    public ArrayList<PlayerFriend> getFriends() {
        return this.friends;
    }

    public ArrayList<PlayerEnemy> getEnemies() {
        return this.enemies;
    }


}