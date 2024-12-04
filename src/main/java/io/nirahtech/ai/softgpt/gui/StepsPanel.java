package io.nirahtech.ai.softgpt.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import javax.swing.JPanel;

import io.nirahtech.ai.softgpt.pipeline.Step;
import io.nirahtech.ai.softgpt.pipeline.Workflow;

public class StepsPanel extends JPanel {

    private final Color backgroundColor;

    private final Workflow workflow;
    private final Set<StepSprite> stepSprites;

    private boolean isAlreadyCalled = false;

    public StepsPanel(final Workflow workflow) {
        Objects.requireNonNull(workflow);
        this.workflow = workflow;
        this.stepSprites = new LinkedHashSet<>();
        final int padding = 150;
        int margin = padding;
        for (Step step : this.workflow.getSteps()) {
            final StepSprite stepSprite = new StepSprite(step);
            this.stepSprites.add(stepSprite);
            stepSprite.setCenter(new Point(margin, 0));
            margin += padding;
        }
        this.backgroundColor = this.getBackground();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!this.isAlreadyCalled) {
            this.setCen
        }
        StepSprite previous = null;
        for (StepSprite sprite : this.stepSprites) {
            if (Objects.nonNull(previous)) {
                g.setColor(Color.BLUE);
                g.drawLine(previous.getCenterPosition().x, previous.getCenterPosition().y, sprite.getCenterPosition().x, sprite.getCenterPosition().y);
            }
            sprite.paintComponent(g);
            previous = sprite;
        }
    }
    
}