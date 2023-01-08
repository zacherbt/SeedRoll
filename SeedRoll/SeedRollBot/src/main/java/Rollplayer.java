import org.jetbrains.annotations.NotNull;

public class Rollplayer implements Comparable<Rollplayer>{

    private final String discordID;
    private int priority;
    private boolean playedLastWeek;

    public Rollplayer(String discordID, boolean playing) {
        this.discordID = discordID;
        priority = 0;
        playedLastWeek = playing;
    }

    public String getID() {
        return discordID;
    }

    public int getPriority() {
        return priority;
    }

    public void increasePriority() {
        priority++;
    }

    public void decreasePriority() {
        if (priority > 0) {
            priority--;
        }
    }

    public boolean playedLastWeek() {
        return playedLastWeek;
    }

    public void setParticipation(boolean played) {
        playedLastWeek = played;
    }

    public int compareTo(@NotNull Rollplayer o) {
        return o.getPriority() - this.getPriority();
    }
}
