package com.chunkslab.realms.api.player.exceptions;

public class UnexistingPlayerException extends Exception {
    @Override
    public String getMessage() {
        return "This player has never played before";
    }
}
