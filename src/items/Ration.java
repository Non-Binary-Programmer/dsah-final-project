package src.items;

import src.ItemBase;
import src.Player;
import src.iteminterfaces.Food;

public class Ration extends ItemBase implements Food {
    public Ration(int count) {
        super(',', 3, 40, count);
    }

    @Override
    public String getName() {
        return "Ration";
    }

    @Override
    public void eat(Player player) {
        player.feed(0.3);
    }
}
