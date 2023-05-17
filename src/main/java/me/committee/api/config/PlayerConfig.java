package me.committee.api.config;

import me.committee.api.player.PlayerEnemy;
import me.committee.api.player.PlayerFriend;

import java.util.ArrayList;

public class PlayerConfig {

    public ArrayList<PlayerFriend> playerFriends;
    public ArrayList<PlayerEnemy> playerEnemies;

    public PlayerConfig(ArrayList<PlayerFriend> playerFriends, ArrayList<PlayerEnemy> playerEnemies) {
        this.playerFriends = playerFriends;
        this.playerEnemies = playerEnemies;
    }

}
