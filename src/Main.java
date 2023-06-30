import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    final static int WORD_LENGTH = 100000;
    final static int TEXT_LENGTH = 10000;

    public static BlockingQueue<String> aQueue = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> bQueue = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> cQueue = new ArrayBlockingQueue<>(100);

    public static void main(String[] args) throws InterruptedException {

        new Thread(() -> {
            for (int i = 0; i < TEXT_LENGTH; i++) {
                String text = generateText("abc", WORD_LENGTH);
                try {
                    aQueue.put(text);
                    bQueue.put(text);
                    cQueue.put(text);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }).start();

        Thread a = getThread(aQueue, 'a');
        Thread b = getThread(bQueue, 'b');
        Thread c = getThread(cQueue, 'c');

        a.start();
        b.start();
        c.start();

        a.join();
        b.join();
        c.join();
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static int freqCount(String text, char c) {
        int freqCount = 0;

        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == c) {
                freqCount++;
            }
        }
        return freqCount;
    }

    public static Thread getThread(BlockingQueue<String> queue, char letter){
        return new Thread(() -> {
            int maxNumber = 0;
            int count;
            String text;
            try {
                for (int i = 0; i < TEXT_LENGTH; i++) {
                    text = queue.take();
                    count = freqCount(text, letter);
                    if (count > maxNumber) maxNumber = count;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Max number of " + letter + " in all texts: " + maxNumber);
        });
    }
}
