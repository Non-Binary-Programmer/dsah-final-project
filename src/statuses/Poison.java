package src.statuses;

import src.Entity;
import src.GamePanel;
import src.Player;
import src.Status;

public class Poison implements Status {
    private int remaining;

    public Poison (int severity) {
        this.remaining = severity;
    }

    @Override
    public void onApply(Entity e) {
        if (e instanceof Player) {
            GamePanel.addMessage("You have been poisoned!");
        }
    }

    @Override
    public void repeatApply(Entity e, Status existing) {
        if (existing instanceof Poison p) {
            p.remaining += this.remaining;
        } else {
            throw new IllegalArgumentException("repeatApply called with two different statuses!");
        }
    }

    @Override
    public void eachTurn(Entity e) {
        e.setHealth(e.getHealth() - remaining / 20 + 1);
        remaining -= remaining / 20 + 1;
        if (remaining <= 0) {
            e.removeStatus(this);
        }
    }

    @Override
    public void onEnd(Entity e) {
    }

    /**
     * Alter the duration of this Poison. This is relative to the current duration, and does *not* set the duration
     * @param modification The amount the duration should be increased or decreased. Positive increases.
     */
    public void modifyDuration(int modification) {
        this.remaining += modification;
    }
}
