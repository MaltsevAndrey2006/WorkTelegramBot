package andrey.dev.worktelegrambot.telegram;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

@Component
public class UpdateConsumer implements LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;

    public UpdateConsumer(@Value("${telegram.bot.token}") String token) {
        this.telegramClient = new OkHttpTelegramClient(token);
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage()) {

            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            switch (messageText) {
                case "/start" -> sendStartMenu(chatId);
                default -> sendErrorMessage(chatId, "я вас не понимаю. Вы можете  ввести команду /start ");
            }
        }
    }

    @SneakyThrows
    private void sendStartMenu(Long chatId) {
        telegramClient.execute(SendMessage
                .builder()
                .text("Выбирите , вы ищите работу(программист) или вы ищите программиста(HR)")
                .chatId(chatId)
                .replyMarkup
                        (ReplyKeyboardMarkup.builder()
                                .keyboard(List.of(new KeyboardRow("программист", "HR")))
                                .resizeKeyboard(true)
                                .oneTimeKeyboard(true)
                                .build())
                .build());
    }

    @SneakyThrows
    private void sendErrorMessage(Long chatId, String mainMessage) {
        SendMessage message = SendMessage.builder().text(mainMessage).chatId(chatId).build();
        telegramClient.execute(message);
    }
}
