const util = require("util")

const fs = require("fs")

const readline = require("readline");
const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

const question = util.promisify(rl.question).bind(rl)

async function compareLangFiles(name_old, name_new) {
    let oldL = fs.readFileSync("./en_US_" + name_old + ".lang")
    let newL = fs.readFileSync("./en_US_" + name_new + ".lang")

    let oldLlines = oldL.toString().split("\n")
    let newLlines = newL.toString().split("\n")

    let oldMap = {}
    let newMap = {}

    for(let i in oldLlines) {
        if (!oldLlines[i].includes("=")) continue
        let o = oldLlines[i].split("=")
        oldMap[o[0]] = o[1].trim()
    }

    for(let i in newLlines) {
        if (!newLlines[i].includes("=")) continue
        let o = newLlines[i].split("=")
        newMap[o[0]] = o[1].trim()
    }
    
    let mapping = {}

    console.log("===============================")
    console.log("=======Changing mappings=======")
    mapping["change"] = await sameValueDifferentKey(oldMap, newMap)
    console.log("===============================")
    console.log("=======Similar mappings========")
    mapping["same"] = await sameKey(oldMap, newMap)
    console.log("===============================")
    console.log("=======Removed mappings========")
    mapping["remove"] = await removeKey(oldMap, newMap)
    console.log("===============================")
    console.log("========Added mappings=========")
    mapping["add"] = await addKey(oldMap, newMap)

    console.log()
    console.log()

    for (let key in mapping["remove"]) {
        if (mapping["remove"][key] == "m") {
            mapping["remove"][key] = null

            let str = `Mutate key ${key} to: `

            try {
                let answer = (await question(str)).toLowerCase();
            } catch(answer) {
                mapping["change"][key] = {
                    key: answer,
                    mode: "t"
                }
            }
        }
    }

    fs.writeFileSync(`./diff-${name_old}-${name_new}.json`, JSON.stringify(mapping, 4))

    rl.close()
}

async function addKey(oldMap, newMap) {
    let object = {}

    for (let key in oldMap) {
        if (newMap[key]) continue

        let str = `An old key has been detected:
Key: ${key}
Value: ${oldMap[key]}
What do you want to do?
[(S)kip/(a)dd/(sa) skip and add]`

        try {
            let answer = (await question(str)).toLowerCase();
        } catch(answer) {
            if (answer == "a") object[key] = "a"
            else if (answer == "sa") object[key] = "sa"
            else object[key] = "s"
        }
    }

    return object;
}

async function removeKey(oldMap, newMap) {
    let removed = {}

    for (let ne in newMap) {
        if (!oldMap[ne]) {
            let str = `A new key has been detected:
Key: ${ne}
Value: ${newMap[ne]}
What do you want to do?
[(R)emove/(s)kip/(rs) remove and skip/(m)utate]\n`

            try {
                let answer = (await question(str)).toLowerCase();
            } catch(answer) {
                if (answer == "s") removed[ne] = "s"
                else if (answer == "rs") removed[ne] = "rs"
                else if (answer == "m") removed[ne] = "m"
                else removed[ne] = "r"
            }
        }
    }

    return removed;
}

async function sameKey(oldMap, newMap) {
    let sameKeyVal = {}

    for (let ol in oldMap) {
        if (!newMap[ol]) continue
        let olVal = oldMap[ol]

        let neVal = newMap[ol];

        if (olVal == neVal) continue

        let str = `The value of the key ${ol}
changed between the two versions
Old: ${olVal}
New: ${neVal}
What do you want to do?
[(U)se new value/(uk) use new value and keep old one/(k)eep old value/(s)kip]\n`;
        try {
            let answer = (await question(str)).toLowerCase();
        } catch(answer) {
            if (answer == "k") sameKeyVal[ol] = "k"
            else if (answer == "s") sameKeyVal[ol] = "s"
            else if (answer == "uk") sameKeyVal[ol] = "uk"
            else sameKeyVal[ol] = "u"
        }
    }

    return sameKeyVal
}

async function sameValueDifferentKey(oldMap, newMap) {
    let object = {}

    for (let ol in oldMap) {
        let olVal = oldMap[ol];

        if (newMap[ol] && newMap[ol] == olVal) continue

        for (let ne in newMap) {
            let neVal = newMap[ne];

            if (olVal == neVal && ol != ne) {
                let str = `Keys (Old) ${ol}
and (New) ${ne}
have the same value: ${olVal}
What do you want to do?
[(T)ransfer/(s)kip/(ts) transfer and skip]\n`

                try {
                    let answer = (await question(str)).toLowerCase();
                } catch(answer) {
                    object[ne] = { key: ol }

                    if (answer == "s") object[ne]["mode"] = "s"
                    else if (answer == "ts") {
                        object[ne]["mode"] = "ts";
                        break
                    } else {
                        object[ne]["mode"] = "t"; 
                        break;
                    }
                }
            }
        }
    }

    return object;
}

compareLangFiles("1.11.2", "1.12.2")