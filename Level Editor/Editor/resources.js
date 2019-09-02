var Types = ["BLOCKS", "ITEMS", "KID", "KILLERS", "MISC", "PLATFORMS", "SAVES", "SLOPES", "UI", "TRIGGERS", "WARPS"];
var Resources = {
   sprBlock: {
       path: "sprites/blocks/sprBlock.png",
       type: "SPRITES",
       subtype: "BLOCKS",
   },
   sprBlock2: {
       path: "sprites/blocks/sprBlock2.png",
       type: "SPRITES",
       subtype: "BLOCKS",
   },
   sprMiniblock: {
       path: "sprites/blocks/sprMiniblock.png",
       type: "SPRITES",
       subtype: "BLOCKS",
   },
   sprSaveBlocker: {
       path: "sprites/blocks/sprSaveBlocker.png",
       type: "SPRITES",
       subtype: "BLOCKS",
   },
   sprSlideBlock: {
       path: "sprites/blocks/sprSlideBlock.png",
       type: "SPRITES",
       subtype: "BLOCKS",
   },
   sprSlideBlockL: {
       path: "sprites/blocks/sprSlideBlockL.png",
       type: "SPRITES",
       subtype: "BLOCKS",
   },
   sprSlideBlockR: {
       path: "sprites/blocks/sprSlideBlockR.png",
       type: "SPRITES",
       subtype: "BLOCKS",
   },
   sprBossItem: {
       path: "sprites/items/sprBossItem.png",
       type: "SPRITES",
       subtype: "ITEMS",
   },
   sprSecretItem: {
       path: "sprites/items/sprSecretItem.png",
       type: "SPRITES",
       subtype: "ITEMS",
   },
   sprBlood: {
       path: "sprites/kid/sprBlood.png",
       type: "SPRITES",
       subtype: "KID",
   },
   sprBow: {
       path: "sprites/kid/sprBow.png",
       type: "SPRITES",
       subtype: "KID",
   },
   sprBullet: {
       path: "sprites/kid/sprBullet.png",
       type: "SPRITES",
       subtype: "KID",
   },
   sprGameOver: {
       path: "sprites/kid/sprGameOver.png",
       type: "SPRITES",
       subtype: "KID",
   },
   sprPlayerFall: {
       path: "sprites/kid/sprPlayerFall.png",
       type: "SPRITES",
       subtype: "KID",
   },
   sprPlayerIdle copy: {
       path: "sprites/kid/sprPlayerIdle copy.png",
       type: "SPRITES",
       subtype: "KID",
   },
   sprPlayerIdle.gif: {
       path: "sprites/kid/sprPlayerIdle.gif",
       type: "SPRITES",
       subtype: "KID",
   },
   sprPlayerJump: {
       path: "sprites/kid/sprPlayerJump.png",
       type: "SPRITES",
       subtype: "KID",
   },
   sprPlayerRunning: {
       path: "sprites/kid/sprPlayerRunning.png",
       type: "SPRITES",
       subtype: "KID",
   },
   sprPlayerSliding: {
       path: "sprites/kid/sprPlayerSliding.png",
       type: "SPRITES",
       subtype: "KID",
   },
   sprCherry: {
       path: "sprites/killers/sprCherry.png",
       type: "SPRITES",
       subtype: "KILLERS",
   },
   sprCherryWhite: {
       path: "sprites/killers/sprCherryWhite.png",
       type: "SPRITES",
       subtype: "KILLERS",
   },
   sprKillerBlock: {
       path: "sprites/killers/sprKillerBlock.png",
       type: "SPRITES",
       subtype: "KILLERS",
   },
   sprMiniDown: {
       path: "sprites/killers/sprMiniDown.png",
       type: "SPRITES",
       subtype: "KILLERS",
   },
   sprMiniLeft: {
       path: "sprites/killers/sprMiniLeft.png",
       type: "SPRITES",
       subtype: "KILLERS",
   },
   sprMiniRight: {
       path: "sprites/killers/sprMiniRight.png",
       type: "SPRITES",
       subtype: "KILLERS",
   },
   sprMiniUp: {
       path: "sprites/killers/sprMiniUp.png",
       type: "SPRITES",
       subtype: "KILLERS",
   },
   sprSpikeDown: {
       path: "sprites/killers/sprSpikeDown.png",
       type: "SPRITES",
       subtype: "KILLERS",
   },
   sprSpikeLeft: {
       path: "sprites/killers/sprSpikeLeft.png",
       type: "SPRITES",
       subtype: "KILLERS",
   },
   sprSpikeRight: {
       path: "sprites/killers/sprSpikeRight.png",
       type: "SPRITES",
       subtype: "KILLERS",
   },
   sprSpikeUp: {
       path: "sprites/killers/sprSpikeUp.png",
       type: "SPRITES",
       subtype: "KILLERS",
   },
   sprGravityDown: {
       path: "sprites/misc/sprGravityDown.png",
       type: "SPRITES",
       subtype: "MISC",
   },
   sprGravityUp: {
       path: "sprites/misc/sprGravityUp.png",
       type: "SPRITES",
       subtype: "MISC",
   },
   sprJumpRefresher: {
       path: "sprites/misc/sprJumpRefresher.png",
       type: "SPRITES",
       subtype: "MISC",
   },
   sprSign: {
       path: "sprites/misc/sprSign.png",
       type: "SPRITES",
       subtype: "MISC",
   },
   sprWalljumpL: {
       path: "sprites/misc/sprWalljumpL.png",
       type: "SPRITES",
       subtype: "MISC",
   },
   sprWalljumpR: {
       path: "sprites/misc/sprWalljumpR.png",
       type: "SPRITES",
       subtype: "MISC",
   },
   sprWater: {
       path: "sprites/misc/sprWater.png",
       type: "SPRITES",
       subtype: "MISC",
   },
   sprMovingPlatform: {
       path: "sprites/platforms/sprMovingPlatform.png",
       type: "SPRITES",
       subtype: "PLATFORMS",
   },
   sprPlatform: {
       path: "sprites/platforms/sprPlatform.png",
       type: "SPRITES",
       subtype: "PLATFORMS",
   },
   sprSave: {
       path: "sprites/saves/sprSave.png",
       type: "SPRITES",
       subtype: "SAVES",
   },
   sprSaveFlip: {
       path: "sprites/saves/sprSaveFlip.png",
       type: "SPRITES",
       subtype: "SAVES",
   },
   sprSaveMedium: {
       path: "sprites/saves/sprSaveMedium.png",
       type: "SPRITES",
       subtype: "SAVES",
   },
   sprSaveMediumFlip: {
       path: "sprites/saves/sprSaveMediumFlip.png",
       type: "SPRITES",
       subtype: "SAVES",
   },
   sprSaveVHard: {
       path: "sprites/saves/sprSaveVHard.png",
       type: "SPRITES",
       subtype: "SAVES",
   },
   sprSaveVHardFlip: {
       path: "sprites/saves/sprSaveVHardFlip.png",
       type: "SPRITES",
       subtype: "SAVES",
   },
   sprSlopeBlock: {
       path: "sprites/slopes/sprSlopeBlock.png",
       type: "SPRITES",
       subtype: "SLOPES",
   },
   sprSlopeDownLeft: {
       path: "sprites/slopes/sprSlopeDownLeft.png",
       type: "SPRITES",
       subtype: "SLOPES",
   },
   sprSlopeDownRight: {
       path: "sprites/slopes/sprSlopeDownRight.png",
       type: "SPRITES",
       subtype: "SLOPES",
   },
   sprSlopeUpLeft: {
       path: "sprites/slopes/sprSlopeUpLeft.png",
       type: "SPRITES",
       subtype: "SLOPES",
   },
   sprSlopeUpRight: {
       path: "sprites/slopes/sprSlopeUpRight.png",
       type: "SPRITES",
       subtype: "SLOPES",
   },
   sprButton_strip2: {
       path: "sprites/triggers/sprButton_strip2.png",
       type: "SPRITES",
       subtype: "TRIGGERS",
   },
   sprSpikeTriggerDown: {
       path: "sprites/triggers/sprSpikeTriggerDown.png",
       type: "SPRITES",
       subtype: "TRIGGERS",
   },
   sprSpikeTriggerLeft: {
       path: "sprites/triggers/sprSpikeTriggerLeft.png",
       type: "SPRITES",
       subtype: "TRIGGERS",
   },
   sprSpikeTriggerRight: {
       path: "sprites/triggers/sprSpikeTriggerRight.png",
       type: "SPRITES",
       subtype: "TRIGGERS",
   },
   sprSpikeTriggerUp: {
       path: "sprites/triggers/sprSpikeTriggerUp.png",
       type: "SPRITES",
       subtype: "TRIGGERS",
   },
   sprTriggerMask: {
       path: "sprites/triggers/sprTriggerMask.png",
       type: "SPRITES",
       subtype: "TRIGGERS",
   },
   loadingScreen: {
       path: "sprites/ui/loadingScreen.png",
       type: "SPRITES",
       subtype: "UI",
   },
   sprWarp: {
       path: "sprites/warps/sprWarp.png",
       type: "SPRITES",
       subtype: "WARPS",
   }
}
