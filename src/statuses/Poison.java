package src.statuses;

import src.*;

public class Poison implements Status {
    private int remaining;
    private Entity source;

    public Poison (int severity, Entity source) {
        this.remaining = severity;
        this.source = source;
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
        if (remaining <= 0) {
            e.removeStatus(this);
            return;
        }
        GamePanel.addMessage("The poison spreads in your body!");
        e.takeDamage(remaining / 20 + 1, source);
        remaining -= remaining / 20 + 1;
        if (remaining <= 0) {
            e.removeStatus(this);
        }
    }

    @Override
    public void onEnd(Entity e) {
        if (e instanceof Player) {
            GamePanel.addMessage("Your body has purged the poison.");
        }
    }

    @Override
    public String getName() {
        return "Poison";
    }

    /**
     * Alter the duration of this Poison. This is relative to the current duration, and does *not* set the duration
     * @param modification The amount the duration should be increased or decreased. Positive increases.
     */
    public void modifyDuration(int modification) {
        this.remaining += modification;
    }
}
