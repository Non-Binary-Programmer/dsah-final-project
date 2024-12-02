package src;

public interface Status {
    /**
     * Do things when this status effect is applied. Any stat modifications should occur here.
     * @param e The Entity the Status is applied to.
     */
    public void onApply(Entity e);

    /**
     * Handle logic for when the same Status is applied twice.
     * @param e The Entity the Status is applied to.
     * @param existing The Status instance currently applied to the Entity.
     */
    public void repeatApply(Entity e, Status existing);

    public void eachTurn(Entity e);

    /**
     * Do things when this status effect is cured. Any stat modifications should be reverted here.
     * @param e The Entity this Status is applied to.
     */
    public void onEnd(Entity e);
}
