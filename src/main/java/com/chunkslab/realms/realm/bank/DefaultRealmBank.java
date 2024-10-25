package com.chunkslab.realms.realm.bank;

import com.chunkslab.realms.api.player.objects.RealmPlayer;
import com.chunkslab.realms.api.realm.Realm;
import com.chunkslab.realms.api.realm.bank.RealmBank;
import com.chunkslab.realms.api.realm.bank.log.BankAction;
import com.chunkslab.realms.api.realm.bank.log.BankLog;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Deque;

@Getter
@RequiredArgsConstructor
public class DefaultRealmBank implements RealmBank {
    @Getter(AccessLevel.PROTECTED)
    private final Realm realm;

    private final Deque<BankLog> logs = new ArrayDeque<>();

    @Setter private BigDecimal balance = BigDecimal.ZERO;

    @Override
    public void deposit(RealmPlayer player, BigDecimal amount) {
        balance = balance.add(amount);
        addLog(new BankLog(player, BankAction.DEPOSIT, amount, System.currentTimeMillis()));
    }

    @Override
    public void withdraw(RealmPlayer player, BigDecimal amount) {
        balance = balance.subtract(amount);
        addLog(new BankLog(player, BankAction.WITHDRAW, amount, System.currentTimeMillis()));
    }

    @Override
    public void transfer(RealmPlayer player, RealmBank target, BigDecimal amount) {
        withdraw(player,amount);
        target.deposit(player, amount);
        addLog(new BankLog(player, BankAction.TRANSFER, amount, System.currentTimeMillis()));
    }

    @Override
    public void addLog(BankLog log) {
        if (logs.size() >= 5)
            logs.removeLast();

        logs.addFirst(log);
    }
}
