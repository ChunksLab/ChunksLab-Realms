package com.chunkslab.realms.api.realm.bank.log;

import com.chunkslab.realms.api.player.objects.RealmPlayer;

import java.math.BigDecimal;

public record BankLog(RealmPlayer player, BankAction action, BigDecimal amount, long timestamp) {}