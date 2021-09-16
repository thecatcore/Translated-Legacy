import fs from "fs";

import fetch from "node-fetch";

const order = [
    {version:"1.12.2", lowercase:true},
    {version:"1.11.2", lowercase:true},
    {version:"1.10.2"},
    {version:"1.9.4"},
    {version:"1.8.9"},
    {version:"1.7.10"},
    {version:"1.6.4"},
    {version:"1.5.2", inJar: true},
    {version:"1.4.7", inJar: true},
    {version:"1.3.2", inJar: true},
    {version:"1.2.5", inJar: true},
    {version:"1.1", inJar: true},
    {version:"b1.7.3", inJar: true}
]

const assetsURL = "https://resources.download.minecraft.net/{0}/{1}";

async function loadManifest(version) {
    let versions = (await(await fetch("https://launchermeta.mojang.com/mc/game/version_manifest_v2.json")).json()).versions;
    let versionURL = versions.find((val, index, array) => {
        return array[index].id == version.version
    }).url;
    let versionManifest = (await(await fetch(versionURL)).json())
    
    if (version.inJar) {

    } else {
        let assetIndexURL = versionManifest.assetIndex.url;
        let assetIndex = (await(await fetch(assetIndexURL)).json()).objects

        let mcmetaURL = hashToURL(assetIndex["pack.mcmeta"].hash)
        console.log(mcmetaURL)
    }
}

function hashToURL(hash) {
    let small = hash.substring(0, 2)

    return assetsURL.replace("{0}", small).replace("{1}", hash)
}

async function convert(name_old, name_new) {
    let diffFile = JSON.parse(fs.readFileSync(`./diff-${name_old}-${name_new}.json`))

    let lFile = fs.readFileSync(`./en_US_${name_new}.lang`);

    let lines = lFile.toString().split("\n")

    let map = {}

    for (let i in lines) {
        if (!lines[i].includes("=")) continue
        let line = lines[i].trim().split("=")
        map[line[0]] = line[1]
    }

    map = await transform(diffFile["change"], map)
    map = await sameKey(diffFile["same"], map)
    map = await removeKey(diffFile["remove"], map)

    fs.writeFileSync(`./en_US_${name_old}_new.lang`, toLangFormat(map))
}

async function transform(diffFileChange, map) {
    for (let neKey in diffFileChange) {
        let info = diffFileChange[neKey];

        if (info.mode == "t") {
            map[info.key] = map[neKey]
            map[neKey] = null
        }
    }

    return map;
}

async function sameKey(diffFileSame, map) {
    for (let key in diffFileSame) {
        let mode = diffFileSame[key]

        if (mode.includes("u")) {
            map[key] = map[key]
        } else if (mode == "s") {
            continue
        }
    }

    return map;
}

async function removeKey(diffFileRemove, map) {
    for (let key in diffFileRemove) {
        let mode = diffFileRemove[key]

        if (mode == "s" || !mode) continue
        else if (mode == "r") map[key] = null
    }

    return map;
}

function toLangFormat(map) {
    let str = ""

    for (let key in map) {
        if (map[key]) {
            str += "\n" + key + "=" + map[key]
        }
    }

    return str
}

// convert("1.1", "1.2.5")
loadManifest(order[6])