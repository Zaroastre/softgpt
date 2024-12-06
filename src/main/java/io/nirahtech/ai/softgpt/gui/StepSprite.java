package io.nirahtech.ai.softgpt.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import io.nirahtech.ai.softgpt.pipeline.Step;
import io.nirahtech.ai.softgpt.pipeline.StepState;

public final class StepSprite {

    private final Map<StepState, Color> colorByStep;
    private final Step step;
    private final int stepRadius;
    private final Point centerPosition;
    private final int fontSize;
    private final String fontName;

    private final BufferedImage image;

    public StepSprite(final Step step) {
        this.step = step;
        this.colorByStep = new HashMap<>();
        this.colorByStep.put(StepState.PENDING, Color.GRAY);
        this.colorByStep.put(StepState.RUNNING, Color.YELLOW);
        this.colorByStep.put(StepState.SUCCESS, Color.GREEN);
        this.colorByStep.put(StepState.FAILED, Color.RED);
        this.stepRadius = 20;
        this.fontSize = 14;
        this.centerPosition = new Point();
        this.image = new BufferedImage((this.stepRadius*2)+1, (this.stepRadius*2)+1, BufferedImage.TYPE_INT_ARGB);
        this.fontName = "Arial";
    }

    public void setCenter(final Point centerPoint) {
        this.centerPosition.setLocation(centerPoint);
    }

    /**
     * @return the centerPosition
     */
    public Point getCenterPosition() {
        return centerPosition;
    }

    /**
     * @return the step
     */
    public Step getStep() {
        return step;
    }

    private final int computeDiameter() {
        return this.stepRadius*2;
    }

    public void paintComponent(final Graphics graphics) {
        final Graphics2D graphics2d = (Graphics2D) graphics; 
        final Graphics2D texture = this.image.createGraphics();
        texture.setColor(this.colorByStep.get(this.step.getStepState()));
        texture.fillOval(0, 0, this.computeDiameter(), this.computeDiameter());
        texture.dispose();

        graphics2d.setColor(Color.WHITE);
        graphics2d.setFont(new Font(this.fontName, 0, this.fontSize));
        graphics2d.drawString(this.step.getName(), this.centerPosition.x - this.stepRadius, this.centerPosition.y + (this.stepRadius*2) + (this.fontSize));
        
        graphics.drawImage(this.image, (this.centerPosition.x-this.stepRadius), (this.centerPosition.y-this.stepRadius), null);
        
    }
}
