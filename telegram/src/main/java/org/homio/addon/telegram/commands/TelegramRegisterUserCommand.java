package org.homio.addon.telegram.commands;

import lombok.extern.log4j.Log4j2;
import org.homio.addon.telegram.TelegramEntity.TelegramUser;
import org.homio.addon.telegram.service.TelegramService;
import org.homio.api.Context;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Log4j2
public final class TelegramRegisterUserCommand extends TelegramBaseCommand {

    private final Context context;

    public TelegramRegisterUserCommand(Context context, TelegramService.TelegramBot telegramBot) {
        super("register", "Register user", telegramBot);
        this.context = context;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings, StringBuilder sb, SendMessage message) {
        TelegramUser entity = telegramBot.getTelegramEntity().getUser(user.getId());
        if (entity != null) {
            sb.append("User: <").append(entity.getName()).append("> already registered");
            log.info("Telegram user <{}> already registered", entity.getName());
        } else {
            telegramBot.getTelegramEntity().addUser(user.getId(), user.getFirstName(),
                    user.getLastName(), Long.toString(chat.getId()));
            context.db().save(telegramBot.getTelegramEntity());
            sb.append("User <").append(user.getFirstName()).append("> has been registered successfully.");
            log.info("Telegram user <{}> has been registered successfully.", user.getFirstName());
        }
    }
}
