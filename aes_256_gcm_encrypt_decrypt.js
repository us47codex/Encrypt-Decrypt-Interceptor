// const crypto = require('crypto');
import crypto from "crypto";

const aes256gcm = (key) => {

    /**
     * Length of The Initialization Vector
     * @var string
    */
    const iv_length = 16;
    /**
     * The length of the authentication tag.
     * @var string
     */
    const tag_length = 12;

    const encrypt = (str) => {

        const iv = new crypto.randomBytes(12);
        const cipher = crypto.createCipheriv('aes-256-gcm', key, iv);

        let enc1 = cipher.update(str, 'utf8');
        let enc2 = cipher.final();
        console.log("encrypt tag length: " + cipher.getAuthTag().length)
        return Buffer.concat([enc1, enc2, iv, cipher.getAuthTag()]).toString("base64");
    };

    const decrypt = (enc) => {
        enc = Buffer.from(enc, "base64");
        console.log("decrypt enc: " + enc);

        const iv = enc.slice(enc.length - 28, enc.length - 16);
        console.log("decrypt iv: " + iv);

        const tag = enc.slice(enc.length - 16);
        console.log("decrypt tag: " + tag);

        enc = enc.slice(0, enc.length - 28);
        console.log("decrypt enc: " + enc);

        const decipher = crypto.createDecipheriv('aes-256-gcm', key, iv);
        decipher.setAuthTag(tag);
        let str = decipher.update(enc, null, 'utf8');
        str += decipher.final('utf8');
        return str;
    };




    const encrypt1 = (text, masterkey) => {
        // random initialization vector
        const iv = crypto.randomBytes(16);

        // random salt
        const salt = crypto.randomBytes(64);

        // AES 256 GCM Mode
        const cipher = crypto.createCipheriv('aes-256-gcm', key, iv);

        // encrypt the given text
        const encrypted = Buffer.concat([cipher.update(text, 'utf8'), cipher.final()]);

        // extract the auth tag
        const tag = cipher.getAuthTag();

        // generate output
        return Buffer.concat([iv, tag, encrypted]).toString('base64');
    };

    /**
     * Decrypts text by given key
     * @param String base64 encoded input data
     * @param Buffer masterkey
     * @returns String decrypted (original) text
     */
    const decrypt1 = (encdata, masterkey) => {
        // base64 decoding
        const bData = Buffer.from(encdata, 'base64');

        // convert data to buffers
        const salt = bData.slice(0, 64);
        const iv = bData.slice(64, 80);
        const tag = bData.slice(80, 96);
        const text = bData.slice(96);

        // derive key using; 32 byte key length
        const key = crypto.pbkdf2Sync(masterkey, salt, 2145, 32, 'sha512');

        // AES 256 GCM Mode
        const decipher = crypto.createDecipheriv('aes-256-gcm', key, iv);
        decipher.setAuthTag(tag);

        // encrypt the given text
        const decrypted = decipher.update(text, 'binary', 'utf8') + decipher.final('utf8');

        return decrypted;
    }
    const encrypt2 = (text) => {
        // random initialization vector
        const iv = crypto.randomBytes(iv_length);
        console.log("encrypt2 IV: " + iv)

        // AES 256 GCM Mode
        const cipher = crypto.createCipheriv('aes-256-gcm', key, iv, {
            authTagLength: 12
        });
        console.log("encrypt2 cipher: " + cipher)

        // encrypt the given text
        const encrypted = Buffer.concat([cipher.update(text, 'utf8'), cipher.final()]);
        console.log("encrypt2 encrypted: " + encrypted)

        // extract the auth tag
        const tag = cipher.getAuthTag();
        console.log("encrypt2 tag: " + tag)

        // generate output
        const final = Buffer.concat([iv, encrypted, tag]).toString('base64');
        console.log("encrypt2 final1: " + Buffer.concat([iv, encrypted, tag]))
        console.log("encrypt2 final1: " + Buffer.concat([iv, encrypted, tag]).length)
        return final
    };

    const decrypt2 = (encdata) => {
        // base64 decoding
        const bData = Buffer.from(encdata, 'base64');
        console.log("decrypt2 iv length: " + iv_length + " =>tag length: " + tag_length + " =>bdata length: " + bData.length)

        console.log("decrypt2 bdata: " + bData)

        const iv = bData.slice(0, iv_length);
        console.log("decrypt2 iv: " + iv)

        const text = bData.slice(iv_length, bData.length - tag_length);
        console.log("decrypt2 text: " + text)

        const tag = bData.slice(bData.length - tag_length, bData.length);
        console.log("decrypt2 tag: " + tag)
        console.log("decrypt2 iv length: " + iv.length + " =>tag length: " + tag.length + " =>text length: " + text.length)

        const decipher = crypto.createDecipheriv('aes-256-gcm', key, iv, {
            authTagLength: 12
        });
        decipher.setAuthTag(tag);
        let str = decipher.update(text, null, 'utf8');
        str += decipher.final('utf8');
        return str;
    }
    return {
        encrypt,
        decrypt,
        encrypt1,
        decrypt1,
        encrypt2,
        decrypt2,
    };
};

const key = "mWhZq4t7w!z%C*F-JaNdRgUkXn2r8x5u"
const cipher = aes256gcm(key); // just a test key

const ct1 = cipher.encrypt2("Hello");
console.log("Encrypted: " + ct1);
console.log("=======================");
const pt1 = cipher.decrypt2(ct);
console.log("Decrypted:" + pt1);
