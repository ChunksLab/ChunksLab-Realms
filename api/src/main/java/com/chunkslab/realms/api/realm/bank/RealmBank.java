package com.chunkslab.realms.api.realm.bank;

import com.chunkslab.realms.api.player.objects.RealmPlayer;
import com.chunkslab.realms.api.realm.bank.log.BankLog;

import java.math.BigDecimal;
import java.util.Deque;

public interface RealmBank {

    /**
     * Get the latest 5 logs of this bank.
     * @return logs
     */
    Deque<BankLog> getLogs();

    /**
     * Get the balance of this bank.
     * @return balance
     */
    BigDecimal getBalance();

    /**
     * Set the balance of this bank.
     * @param balance to set
     */
    void setBalance(BigDecimal balance);

    /**
     * Deposit an amount to the bank.
     * @param player who is depositing
     * @param amount to deposit
     */
    void deposit(RealmPlayer player, BigDecimal amount);

    /**
     * Withdraw an amount from the bank.
     * @param player who is withdrawing
     * @param amount to withdraw
     */
    void withdraw(RealmPlayer player, BigDecimal amount);

    /**
     * Transfer an amount from this bank to another.
     * @param player who is transferring
     * @param target target bank
     * @param amount to transfer
     */
    void transfer(RealmPlayer player, RealmBank target, BigDecimal amount);

    /**
     * Add a log to this bank.
     * @param log to add
     */
    void addLog(BankLog log);
}