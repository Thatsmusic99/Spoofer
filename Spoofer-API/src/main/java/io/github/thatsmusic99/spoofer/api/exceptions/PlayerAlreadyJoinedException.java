package io.github.thatsmusic99.spoofer.api.exceptions;

public class PlayerAlreadyJoinedException extends Throwable {

    public PlayerAlreadyJoinedException(String playerName) {
        super("Player " + playerName + " is already in the server!");
    }
}
