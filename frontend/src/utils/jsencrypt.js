import JSEncrypt from 'jsencrypt/bin/jsencrypt.min'

// 密钥对生成 http://web.chacuo.net/netrsakeypair

const publicKey = 'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKoR8mX0rGKLqzcWmOzbfj64K8ZIgOdH\n' +
  'nzkXSOVOZbFu/TJhZ7rFAN+eaGkl3C4buccQd/EjEsj9ir7ijT7h96MCAwEAAQ=='

const publicKey2 = 'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDF4xuwZNWhVaQZguqxLBKhc2S1jKiJOzJS0YczxLPTH9l2Fa+zxZ/AzYci95KG9kFbvZEVyuFhR5UTsORc+ONsqTg/zx2+vJ7kI8h2/qtWNV6vH9JmgMVHl5CEpikXhZjOPutNmzcRKfzDbYwp274f6iDUi9GttlYR68qPBij8AQIDAQAB'


const privateKey = 'MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAqhHyZfSsYourNxaY\n' +
  '7Nt+PrgrxkiA50efORdI5U5lsW79MmFnusUA355oaSXcLhu5xxB38SMSyP2KvuKN\n' +
  'PuH3owIDAQABAkAfoiLyL+Z4lf4Myxk6xUDgLaWGximj20CUf+5BKKnlrK+Ed8gA\n' +
  'kM0HqoTt2UZwA5E2MzS4EI2gjfQhz5X28uqxAiEA3wNFxfrCZlSZHb0gn2zDpWow\n' +
  'cSxQAgiCstxGUoOqlW8CIQDDOerGKH5OmCJ4Z21v+F25WaHYPxCFMvwxpcw99Ecv\n' +
  'DQIgIdhDTIqD2jfYjPTY8Jj3EDGPbH2HHuffvflECt3Ek60CIQCFRlCkHpi7hthh\n' +
  'YhovyloRYsM+IS9h/0BzlEAuO0ktMQIgSPT3aFAgJYwKpqRYKlLDVcflZFCKY7u3\n' +
  'UP8iWi1Qw0Y='

const privateKey2 = 'MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAMXjG7Bk1aFVpBmC6rEsEqFzZLWMqIk7MlLRhzPEs9Mf2XYVr7PFn8DNhyL3kob2QVu9kRXK4WFHlROw5Fz442ypOD/PHb68nuQjyHb+q1Y1Xq8f0maAxUeXkISmKReFmM4+602bNxEp/MNtjCnbvh/qINSL0a22VhHryo8GKPwBAgMBAAECgYEArGQGKiwd45fdBpCDbmdrr5T8ZiCsbijQ1U9Njm7NDZ7QbkvELkN6wZcLJA4zazm6hHZnn2mH4Jxgpa/TsZ7tV6KocRq4W/Txxf8c/aLW7X/vm9MyWgXk90mcKM2+hJHj7hnon3KQwpLJMqCJBZXhPhXOoHAaulOPBPpamryxqBUCQQDrECPMzgos00/uL+BVHHmRN/H4X19UStNmcd+ZYVWQJ3OrONYncU++N7qovW3omVflCOSAqghBIBxu6HMR2rLfAkEA14NKc+HdR1vczxNwm66rUsJE2N0NumYQkNOpsz59gERXAQVIakdzvBhyVbBWUoWRFTEpvmcrADzGzLZSyWgNHwJBAIjgYxhVOhOUEM0vz79/z0JCg4IPdyFMWQEkegGEbiB8+JvRgtKA5uBq7yRm+eMht87EeNp7KEdT/XqPnMHeyZ0CQA3e21nd/ZnsJagGPkcm1Mv8yOWURJ/slcBcfx2UNgudWmQsbN66Po6qnCE7ujKReib9BQzGIxRe1QSz71wbIE0CQQDRU/TyLk21xJ59dDVudXEt5EtPBNAAeqWY+I0lnzxAqkeylpK8D2BxFxytLH1p/+XIg8e6CNgz/zKumu1VLvbe'

// 加密
export function encryptForTransfer(txt) {
  const encrypt = new JSEncrypt();
  encrypt.setPublicKey(publicKey2);

  return encrypt.encrypt(txt);
}
// 加密
export function encrypt(txt) {
  const encryptor = new JSEncrypt()
  encryptor.setPublicKey(publicKey) // 设置公钥
  return encryptor.encrypt(txt) // 对数据进行加密
}

// 解密
export function decrypt(txt) {
  const encryptor = new JSEncrypt()
  encryptor.setPrivateKey(privateKey) // 设置私钥
  return encryptor.decrypt(txt) // 对数据进行解密
}
// 解密
export function decryptForTransfer(txt) {
  const encryptor = new JSEncrypt()
  encryptor.setPrivateKey(privateKey2) // 设置私钥
  return encryptor.decrypt(txt) // 对数据进行解密
}

