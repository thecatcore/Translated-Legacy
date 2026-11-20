package fr.catcore.translatedlegacy.ornithe.client.language;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.vertex.Tesselator;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.List;

public abstract class ListWidget {
    private final Minecraft minecraft;
    private final int width;
    private final int height;
    protected final int minY;
    protected final int maxY;
    private final int maxX;
    private final int minX;
    protected final int entryHeight;
    private int upButtonId;
    private int downButtonId;
    private float mouseYStart = -2.0F;
    private float scrollSpeedMultiplier;
    private float scrollAmount;
    private int pos = -1;
    private long time = 0L;
    private boolean renderSelectionHighlight = true;
    private boolean renderHeader;
    private int headerHeight;

    public ListWidget(Minecraft minecraft, int width, int height, int minY, int maxY, int entryHeight) {
        this.minecraft = minecraft;
        this.width = width;
        this.height = height;
        this.minY = minY;
        this.maxY = maxY;
        this.entryHeight = entryHeight;
        this.minX = 0;
        this.maxX = width;
    }

    public void setRenderSelectionHighlight(boolean renderSelectionHighlight) {
        this.renderSelectionHighlight = (boolean)renderSelectionHighlight;
    }

    protected void setHeader(boolean renderHeader, int headerHeight) {
        this.renderHeader = (boolean)renderHeader;
        this.headerHeight = headerHeight;
        if (!renderHeader) {
            this.headerHeight = 0;
        }

    }

    protected abstract int size();

    protected abstract void entryClicked(int index, boolean doubleClick);

    protected abstract boolean isEntrySelected(int index);

    protected int getHeight() {
        return this.size() * this.entryHeight + this.headerHeight;
    }

    protected abstract void renderBackground();

    protected abstract void renderEntry(int index, int x, int y, int height, Tesselator tesselator);

    protected void renderHeader(int x, int y, Tesselator tesselator) {
    }

    protected void headerClicked(int x, int y) {
    }

    protected void renderDecorations(int mouseX, int mouseY) {
    }

    public int getEntryAt(int x, int y) {
        int i = this.width / 2 - 110;
        int j = this.width / 2 + 110;
        int k = y - this.minY - this.headerHeight + (int)this.scrollAmount - 4;
        int l = k / this.entryHeight;
        return x >= i && x <= j && l >= 0 && k >= 0 && l < this.size() ? l : -1;
    }

    public void setScrollButtonIds(List buttons, int upButtonId, int downButtonId) {
        this.upButtonId = upButtonId;
        this.downButtonId = downButtonId;
    }

    private void capScrolling() {
        int i = this.getHeight() - (this.maxY - this.minY - 4);
        if (i < 0) {
            i /= 2;
        }

        if (this.scrollAmount < 0.0F) {
            this.scrollAmount = 0.0F;
        }

        if (this.scrollAmount > (float)i) {
            this.scrollAmount = (float)i;
        }

    }

    public void buttonClicked(ButtonWidget button) {
        if (button.active) {
            if (button.id == this.upButtonId) {
                this.scrollAmount -= (float)(this.entryHeight * 2 / 3);
                this.mouseYStart = -2.0F;
                this.capScrolling();
            } else if (button.id == this.downButtonId) {
                this.scrollAmount += (float)(this.entryHeight * 2 / 3);
                this.mouseYStart = -2.0F;
                this.capScrolling();
            }

        }
    }

    public void render(int mouseX, int mouseY, float tickDelta) {
        this.renderBackground();
        int i = this.size();
        int j = this.width / 2 + 124;
        int k = j + 6;
        if (Mouse.isButtonDown(0)) {
            if (this.mouseYStart == -1.0F) {
                int l = 1;
                if (mouseY >= this.minY && mouseY <= this.maxY) {
                    int m = this.width / 2 - 110;
                    int n = this.width / 2 + 110;
                    int p = mouseY - this.minY - this.headerHeight + (int)this.scrollAmount - 4;
                    int r = p / this.entryHeight;
                    if (mouseX >= m && mouseX <= n && r >= 0 && p >= 0 && r < i) {
                        boolean t = r == this.pos && System.currentTimeMillis() - this.time < 250L;
                        this.entryClicked(r, t);
                        this.pos = r;
                        this.time = System.currentTimeMillis();
                    } else if (mouseX >= m && mouseX <= n && p < 0) {
                        this.headerClicked(mouseX - m, mouseY - this.minY + (int)this.scrollAmount - 4);
                        l = 0;
                    }

                    if (mouseX >= j && mouseX <= k) {
                        this.scrollSpeedMultiplier = -1.0F;
                        int u = this.getHeight() - (this.maxY - this.minY - 4);
                        if (u < 1) {
                            u = 1;
                        }

                        int x = (int)((float)((this.maxY - this.minY) * (this.maxY - this.minY)) / (float)this.getHeight());
                        if (x < 32) {
                            x = 32;
                        }

                        if (x > this.maxY - this.minY - 8) {
                            x = this.maxY - this.minY - 8;
                        }

                        this.scrollSpeedMultiplier /= (float)(this.maxY - this.minY - x) / (float)u;
                    } else {
                        this.scrollSpeedMultiplier = 1.0F;
                    }

                    if (l != 0) {
                        this.mouseYStart = (float)mouseY;
                    } else {
                        this.mouseYStart = -2.0F;
                    }
                } else {
                    this.mouseYStart = -2.0F;
                }
            } else if (this.mouseYStart >= 0.0F) {
                this.scrollAmount -= ((float)mouseY - this.mouseYStart) * this.scrollSpeedMultiplier;
                this.mouseYStart = (float)mouseY;
            }
        } else {
            while (Mouse.next()) {
                int var7 = Mouse.getEventDWheel();
                if (var7 != 0) {
                    if (var7 > 0) {
                        var7 = -1;
                    } else if (var7 < 0) {
                        var7 = 1;
                    }

                    this.scrollAmount += (float)(var7 * this.entryHeight / 2);
                }
            }

            this.mouseYStart = -1.0F;
        }

        this.capScrolling();
        GL11.glDisable(2896);
        GL11.glDisable(2912);
        Tesselator tesselator = Tesselator.INSTANCE;
        GL11.glBindTexture(3553, this.minecraft.textureManager.load("/gui/background.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32.0F;
        tesselator.begin();
        tesselator.color(2105376);
        tesselator.vertex((double)this.minX, (double)this.maxY, (double)0.0F, (double)((float)this.minX / f), (double)((float)(this.maxY + (int)this.scrollAmount) / f));
        tesselator.vertex((double)this.maxX, (double)this.maxY, (double)0.0F, (double)((float)this.maxX / f), (double)((float)(this.maxY + (int)this.scrollAmount) / f));
        tesselator.vertex((double)this.maxX, (double)this.minY, (double)0.0F, (double)((float)this.maxX / f), (double)((float)(this.minY + (int)this.scrollAmount) / f));
        tesselator.vertex((double)this.minX, (double)this.minY, (double)0.0F, (double)((float)this.minX / f), (double)((float)(this.minY + (int)this.scrollAmount) / f));
        tesselator.end();
        int o = this.width / 2 - 92 - 16;
        int q = this.minY + 4 - (int)this.scrollAmount;
        if (this.renderHeader) {
            this.renderHeader(o, q, tesselator);
        }

        for(int s = 0; s < i; ++s) {
            int v = q + s * this.entryHeight + this.headerHeight;
            int y = this.entryHeight - 4;
            if (v <= this.maxY && v + y >= this.minY) {
                if (this.renderSelectionHighlight && this.isEntrySelected(s)) {
                    int a = this.width / 2 - 110;
                    int c = this.width / 2 + 110;
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    GL11.glDisable(3553);
                    tesselator.begin();
                    tesselator.color(8421504);
                    tesselator.vertex((double)a, (double)(v + y + 2), (double)0.0F, (double)0.0F, (double)1.0F);
                    tesselator.vertex((double)c, (double)(v + y + 2), (double)0.0F, (double)1.0F, (double)1.0F);
                    tesselator.vertex((double)c, (double)(v - 2), (double)0.0F, (double)1.0F, (double)0.0F);
                    tesselator.vertex((double)a, (double)(v - 2), (double)0.0F, (double)0.0F, (double)0.0F);
                    tesselator.color(0);
                    tesselator.vertex((double)(a + 1), (double)(v + y + 1), (double)0.0F, (double)0.0F, (double)1.0F);
                    tesselator.vertex((double)(c - 1), (double)(v + y + 1), (double)0.0F, (double)1.0F, (double)1.0F);
                    tesselator.vertex((double)(c - 1), (double)(v - 1), (double)0.0F, (double)1.0F, (double)0.0F);
                    tesselator.vertex((double)(a + 1), (double)(v - 1), (double)0.0F, (double)0.0F, (double)0.0F);
                    tesselator.end();
                    GL11.glEnable(3553);
                }

                this.renderEntry(s, o, v, y, tesselator);
            }
        }

        GL11.glDisable(2929);
        int var21 = 4;
        this.renderHoleBackground(0, this.minY, 255, 255);
        this.renderHoleBackground(this.maxY, this.height, 255, 255);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3008);
        GL11.glShadeModel(7425);
        GL11.glDisable(3553);
        tesselator.begin();
        tesselator.color(0, 0);
        tesselator.vertex((double)this.minX, (double)(this.minY + var21), (double)0.0F, (double)0.0F, (double)1.0F);
        tesselator.vertex((double)this.maxX, (double)(this.minY + var21), (double)0.0F, (double)1.0F, (double)1.0F);
        tesselator.color(0, 255);
        tesselator.vertex((double)this.maxX, (double)this.minY, (double)0.0F, (double)1.0F, (double)0.0F);
        tesselator.vertex((double)this.minX, (double)this.minY, (double)0.0F, (double)0.0F, (double)0.0F);
        tesselator.end();
        tesselator.begin();
        tesselator.color(0, 255);
        tesselator.vertex((double)this.minX, (double)this.maxY, (double)0.0F, (double)0.0F, (double)1.0F);
        tesselator.vertex((double)this.maxX, (double)this.maxY, (double)0.0F, (double)1.0F, (double)1.0F);
        tesselator.color(0, 0);
        tesselator.vertex((double)this.maxX, (double)(this.maxY - var21), (double)0.0F, (double)1.0F, (double)0.0F);
        tesselator.vertex((double)this.minX, (double)(this.maxY - var21), (double)0.0F, (double)0.0F, (double)0.0F);
        tesselator.end();
        int w = this.getHeight() - (this.maxY - this.minY - 4);
        if (w > 0) {
            int z = (this.maxY - this.minY) * (this.maxY - this.minY) / this.getHeight();
            if (z < 32) {
                z = 32;
            }

            if (z > this.maxY - this.minY - 8) {
                z = this.maxY - this.minY - 8;
            }

            int b = (int)this.scrollAmount * (this.maxY - this.minY - z) / w + this.minY;
            if (b < this.minY) {
                b = this.minY;
            }

            tesselator.begin();
            tesselator.color(0, 255);
            tesselator.vertex((double)j, (double)this.maxY, (double)0.0F, (double)0.0F, (double)1.0F);
            tesselator.vertex((double)k, (double)this.maxY, (double)0.0F, (double)1.0F, (double)1.0F);
            tesselator.vertex((double)k, (double)this.minY, (double)0.0F, (double)1.0F, (double)0.0F);
            tesselator.vertex((double)j, (double)this.minY, (double)0.0F, (double)0.0F, (double)0.0F);
            tesselator.end();
            tesselator.begin();
            tesselator.color(8421504, 255);
            tesselator.vertex((double)j, (double)(b + z), (double)0.0F, (double)0.0F, (double)1.0F);
            tesselator.vertex((double)k, (double)(b + z), (double)0.0F, (double)1.0F, (double)1.0F);
            tesselator.vertex((double)k, (double)b, (double)0.0F, (double)1.0F, (double)0.0F);
            tesselator.vertex((double)j, (double)b, (double)0.0F, (double)0.0F, (double)0.0F);
            tesselator.end();
            tesselator.begin();
            tesselator.color(12632256, 255);
            tesselator.vertex((double)j, (double)(b + z - 1), (double)0.0F, (double)0.0F, (double)1.0F);
            tesselator.vertex((double)(k - 1), (double)(b + z - 1), (double)0.0F, (double)1.0F, (double)1.0F);
            tesselator.vertex((double)(k - 1), (double)b, (double)0.0F, (double)1.0F, (double)0.0F);
            tesselator.vertex((double)j, (double)b, (double)0.0F, (double)0.0F, (double)0.0F);
            tesselator.end();
        }

        this.renderDecorations(mouseX, mouseY);
        GL11.glEnable(3553);
        GL11.glShadeModel(7424);
        GL11.glEnable(3008);
        GL11.glDisable(3042);
    }

    private void renderHoleBackground(int top, int bottom, int topAlpha, int bottomAlpha) {
        Tesselator tesselator = Tesselator.INSTANCE;
        GL11.glBindTexture(3553, this.minecraft.textureManager.load("/gui/background.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32.0F;
        tesselator.begin();
        tesselator.color(4210752, bottomAlpha);
        tesselator.vertex((double)0.0F, (double)bottom, (double)0.0F, (double)0.0F, (double)((float)bottom / f));
        tesselator.vertex((double)this.width, (double)bottom, (double)0.0F, (double)((float)this.width / f), (double)((float)bottom / f));
        tesselator.color(4210752, topAlpha);
        tesselator.vertex((double)this.width, (double)top, (double)0.0F, (double)((float)this.width / f), (double)((float)top / f));
        tesselator.vertex((double)0.0F, (double)top, (double)0.0F, (double)0.0F, (double)((float)top / f));
        tesselator.end();
    }
}
