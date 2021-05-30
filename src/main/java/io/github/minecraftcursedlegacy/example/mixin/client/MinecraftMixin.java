package io.github.minecraftcursedlegacy.example.mixin.client;

import net.minecraft.client.ClientInteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MovementManager;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.AbstractClientPlayer;
import net.minecraft.entity.player.Player;
import net.minecraft.level.Level;
import net.minecraft.level.chunk.ClientChunkCache;
import net.minecraft.level.dimension.DimensionData;
import net.minecraft.level.source.LevelSource;
import net.minecraft.level.storage.LevelStorage;
import net.minecraft.stat.StatManager;
import net.minecraft.stat.Stats;
import net.minecraft.util.ProgressListenerImpl;
import net.minecraft.util.Vec3i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Shadow public abstract void setLevel(Level arg);

    @Shadow private LevelStorage levelStorage;

    @Shadow protected abstract void convertWorldFormat(String string, String string1);

    @Shadow public StatManager statManager;

    @Shadow public abstract void notifyStatus(Level arg, String string);

    @Shadow public ProgressListenerImpl progressListener;

    @Shadow public Level level;

    @Shadow public AbstractClientPlayer player;

    @Shadow public abstract void switchDimension();

    @Shadow public LivingEntity field_2807;

    @Shadow public ClientInteractionManager interactionManager;

    @Shadow public GameOptions options;

    @Shadow public Screen currentScreen;

    @Shadow public abstract void openScreen(Screen arg);

    /**
     * @author CatCore
     */
    @Overwrite
    public void createOrLoadWorld(String string, String string1, long l) {
        TranslationStorage translationStorage = TranslationStorage.getInstance();

        this.setLevel((Level)null);
        System.gc();
        if (this.levelStorage.isOld(string)) {
            this.convertWorldFormat(string, string1);
        } else {
            DimensionData var5 = this.levelStorage.createDimensionFile(string, false);
            Level var6 = null;
            var6 = new Level(var5, string1, l);
            if (var6.generating) {
                this.statManager.incrementStat(Stats.createWorld, 1);
                this.statManager.incrementStat(Stats.startGame, 1);
                this.notifyStatus(var6, translationStorage.translate("menu.generatingLevel"));
            } else {
                this.statManager.incrementStat(Stats.loadWorld, 1);
                this.statManager.incrementStat(Stats.startGame, 1);
                this.notifyStatus(var6, translationStorage.translate("menu.loadingLevel"));
            }
        }

    }

    /**
     * @author CatCore
     */
    @Overwrite
    private void method_2130(String string) {
        TranslationStorage translationStorage = TranslationStorage.getInstance();

        this.progressListener.notifyWithGameRunning(string);
        this.progressListener.notifySubMessage(translationStorage.translate("menu.generatingTerrain"));
        short var2 = 128;
        int var3 = 0;
        int var4 = var2 * 2 / 16 + 1;
        var4 *= var4;
        LevelSource var5 = this.level.getLevelSource();
        Vec3i var6 = this.level.getSpawnPosition();
        if (this.player != null) {
            var6.x = (int)this.player.x;
            var6.z = (int)this.player.z;
        }

        if (var5 instanceof ClientChunkCache) {
            ClientChunkCache var7 = (ClientChunkCache)var5;
            var7.setSpawnChunk(var6.x >> 4, var6.z >> 4);
        }

        for(int var10 = -var2; var10 <= var2; var10 += 16) {
            for(int var8 = -var2; var8 <= var2; var8 += 16) {
                this.progressListener.progressStagePercentage(var3++ * 100 / var4);
                this.level.getTileId(var6.x + var10, 64, var6.z + var8);

                while(this.level.method_232()) {
                }
            }
        }

        this.progressListener.notifySubMessage(translationStorage.translate("menu.simulating"));
        boolean var9 = true;
        this.level.method_292();
    }

    /**
     * @author CatCore
     */
    @Overwrite
    public void respawn(boolean flag, int i) {
        TranslationStorage translationStorage = TranslationStorage.getInstance();

        if (!this.level.isClient && !this.level.dimension.canPlayerSleep()) {
            this.switchDimension();
        }

        Vec3i var3 = null;
        Vec3i var4 = null;
        boolean var5 = true;
        if (this.player != null && !flag) {
            var3 = this.player.getSpawnPosition();
            if (var3 != null) {
                var4 = Player.getRespawnPos(this.level, var3);
                if (var4 == null) {
                    this.player.sendTranslatedMessage("tile.bed.notValid");
                }
            }
        }

        if (var4 == null) {
            var4 = this.level.getSpawnPosition();
            var5 = false;
        }

        LevelSource var6 = this.level.getLevelSource();
        if (var6 instanceof ClientChunkCache) {
            ClientChunkCache var7 = (ClientChunkCache)var6;
            var7.setSpawnChunk(var4.x >> 4, var4.z >> 4);
        }

        this.level.revalidateSpawnPos();
        this.level.method_295();
        int var8 = 0;
        if (this.player != null) {
            var8 = this.player.entityId;
            this.level.removeEntity(this.player);
        }

        this.field_2807 = null;
        this.player = (AbstractClientPlayer)this.interactionManager.createPlayer(this.level);
        this.player.dimensionId = i;
        this.field_2807 = this.player;
        this.player.afterSpawn();
        if (var5) {
            this.player.setPlayerSpawn(var3);
            this.player.setPositionAndAngles((double)((float)var4.x + 0.5F), (double)((float)var4.y + 0.1F), (double)((float)var4.z + 0.5F), 0.0F, 0.0F);
        }

        this.interactionManager.method_1711(this.player);
        this.level.addPlayer(this.player);
        this.player.keypressManager = new MovementManager(this.options);
        this.player.entityId = var8;
        this.player.method_494();
        this.interactionManager.method_1718(this.player);
        this.method_2130(translationStorage.translate("menu.respawning"));
        if (this.currentScreen instanceof DeathScreen) {
            this.openScreen((Screen)null);
        }

    }
}
