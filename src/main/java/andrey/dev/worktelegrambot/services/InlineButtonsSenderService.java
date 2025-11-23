package andrey.dev.worktelegrambot.services;

import andrey.dev.worktelegrambot.enums.TechStack;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.ArrayList;
import java.util.List;

@Component
public class InlineButtonsSenderService{

    @SneakyThrows
    public void sendTechStackSelection(Long chatId, TelegramClient telegramClient) {

        String messageText = "💻 Выберите технологии которые вы знаете (можно несколько):";

        List<InlineKeyboardRow> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> currentRow = new ArrayList<>();

        int buttonsInRow = 2;

        for (int i = 0; i < TechStack.values().length; i++) {
            TechStack tech = TechStack.values()[i];

            InlineKeyboardButton button = InlineKeyboardButton.builder()
                    .text(tech.getName())
                    .callbackData(tech.name())
                    .build();

            currentRow.add(button);

            if (currentRow.size() == buttonsInRow || i == TechStack.values().length - 1) {
                keyboard.add(new InlineKeyboardRow(currentRow));
                currentRow = new ArrayList<>();
            }
        }

        keyboard.add(new InlineKeyboardRow(
                InlineKeyboardButton.builder()
                        .text("📤 Отправить выбор")
                        .callbackData("tech_submit")
                        .build()
        ));

        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder()
                .keyboard(keyboard)
                .build();

        SendMessage message = SendMessage.builder()
                .chatId(chatId.toString())
                .text(messageText)
                .replyMarkup(markup)
                .build();

        telegramClient.execute(message);
    }
}
