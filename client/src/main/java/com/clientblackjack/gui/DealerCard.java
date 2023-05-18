package com.clientblackjack.gui;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.IOException;

public class DealerCard extends ActorCard {
    private boolean hidden;
    DealerCard() {
        super("Dealer", false);
        // able to customize dealer font/color, other attributes here in the future.
<<<<<<< HEAD
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;              // Avatar
        constraints.gridy = 0;
        constraints.insets = new Insets(0, 0, 0, 10);
        constraints.anchor = GridBagConstraints.WEST;
        this.add(this.avatar_, constraints);

        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.gridx = 0   ;              // Hand/Cards
        constraints.gridy = 1;
        constraints.gridwidth = 5;
        this.add(hand_, constraints);
        this.hidden = false;
=======
        super("Dealer"); // are you gonna implement any more functions here?
>>>>>>> 1ad69b8e79fbbe1e33031e898af745377545a638
    }

    public void hideSecondCard() throws IOException {
        if (this.hand_.getCards().size() >= 2) {
            this.hand_.hideSecondCard();
            this.hand_.repaint();
        }
        hidden = true;
    }

    public void showSecondCard() throws IOException {
        if (this.hand_.getCards().size() >= 2) {
            Card secondCard = this.hand_.getCards().get(1);
            secondCard.setImage(this.hand_.getCards().get(1).getImage());
            this.hand_.repaint();
        }
        hidden = false;
    }

    public boolean hiddenCard() {
        return hidden;
    }

    public void setHidden(boolean b) {
        this.hidden = b;
    }
    

}
