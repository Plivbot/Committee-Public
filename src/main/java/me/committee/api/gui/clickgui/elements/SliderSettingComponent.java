package me.committee.api.gui.clickgui.elements;

import me.committee.Committee;
import me.committee.api.setting.Setting;
import me.committee.api.util.MessageSendHelper;
import me.committee.api.util.RenderUtil;
import me.committee.impl.modules.client.ClickGUI;

import java.util.ArrayList;

public class SliderSettingComponent extends NamedGuiComponent {

    private final Setting<? extends Number> setting;

    private boolean dragging = false;

    public SliderSettingComponent(float relX, float relY, float parentX, float parentY, float width, float height, ArrayList<GuiComponent> childElements, String name, Setting<? extends Number> setting) {
        super(relX, relY, parentX, parentY, width, height, childElements, name);
        this.setting = setting;
    }

    public SliderSettingComponent(float relX, float relY, float parentX, float parentY, float width, float height, String name, Setting<? extends Number> setting) {
        super(relX, relY, parentX, parentY, width, height, name);
        this.setting = setting;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks, boolean renderTooltip, boolean hover, int waitTime) {

        if (hover) {
            RenderUtil.drawRect(this.getX(), this.getY(), this.getWidth(), this.getHeight(), 0xCC551E5B);
        } else {
            RenderUtil.drawRect(this.getX(), this.getY(), this.getWidth(), this.getHeight(), 0xCC111111);
        }

        if (this.isDragging()) {
            final float xDragPos = Math.min(Math.max(mouseX, this.getX()), this.getX() + this.getWidth());

            float sliderPos = (xDragPos - this.getX()) / this.getWidth();

            final boolean snap = (Boolean) Committee.moduleManager.getModuleByClass(ClickGUI.class).getSettingById(0).getValue();


            // todo: remove dup code
            if (setting.getValue() instanceof Integer || setting.getValue() instanceof Long) {
                final int newValue = (int) (((Integer) this.setting.getMax() - (Integer) this.setting.getMin()) * sliderPos + (Integer) this.setting.getMin());
                ((Setting<Integer>) this.setting).setValue(snap ? newValue - (newValue % (Integer) setting.getInc()) : newValue);
            } else if (setting.getValue() instanceof Float) {
                final float newValue = ((Float) this.setting.getMax() - (Float) this.setting.getMin()) * sliderPos + (Float) this.setting.getMin();
                ((Setting<Float>) this.setting).setValue(snap ? newValue - (newValue % (Float) setting.getInc()) : newValue);
            } else if (setting.getValue() instanceof Double) {
                final double newValue = ((Double) this.setting.getMax() - (Double) this.setting.getMin()) * sliderPos + (Double) this.setting.getMin();
                ((Setting<Double>) this.setting).setValue(snap ? newValue - (newValue % (Double) setting.getInc()) : newValue);
            } else {
                MessageSendHelper.sendMessage("Sliders only support type types Float, Double, Long and Integer", MessageSendHelper.Level.WARN);
            }

        }

        // the slider "block" which shows where the current value is
        RenderUtil.drawRect(this.getX(), this.getY(), getBlockSize(), this.getHeight(), 0xCCAC5FB5);

        drawContent(mouseX, mouseY, this.getName() + " : " + this.getSetting().getValue());

        super.render(mouseX, mouseY, partialTicks, renderTooltip, hover, waitTime);
    }

    /**
     * The code here is fucked, but I think I prevented most crashes with casts
     * this re checks the setting's values every time when called
     *
     * @return the x size of the slider
     */
    private float getBlockSize() {
        final float current = setting.getValue().floatValue() - setting.getMin().floatValue();
        final float maxVal = setting.getMax().floatValue() - setting.getMin().floatValue();
        return (current / maxVal) * this.getWidth();
    }

    @Override
    public String getDescription() {
        return setting.getDesc();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (RenderUtil.isInSquare(mouseX, mouseY, this.getX(), this.getY(), this.getWidth(), this.getHeight())) {
            if (mouseButton == 0) this.setDragging(true);
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        this.setDragging(false);

        super.mouseReleased(mouseX, mouseY, state);
    }

    public Setting<? extends Number> getSetting() {
        return setting;
    }

    public boolean isDragging() {
        return dragging;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

}
