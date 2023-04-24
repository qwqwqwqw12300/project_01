class NumberUtil {

    /**
     * 把错误的数据转正
     * @param {*number} num Input number
     */
    strip(num, precision = 12) {
        return +parseFloat(num.toPrecision(precision));
    }

    /**
     * Return digits length of a number
     * @param {*number} num Input number
     */
    digitLength(num) {
        // Get digit length of e
        const eSplit = num.toString().split(/[eE]/);
        const len = (eSplit[0].split('.')[1] || '').length - (+(eSplit[1] || 0));
        return len > 0 ? len : 0;
    }

    /**
     * 把小数转成整数，支持科学计数法。如果是小数则放大成整数
     * @param {*number} num 输入数
     */
    float2Fixed(num) {
        if (num.toString().indexOf('e') === -1) {
            return Number(num.toString().replace('.', ''));
        }
        const dLen = this.digitLength(num);
        return dLen > 0 ? num * Math.pow(10, dLen) : num;
    }

    /**
     * 检测数字是否越界，如果越界给出提示
     * @param {*number} num 输入数
     */
    checkBoundary(num) {
        if (num > Number.MAX_SAFE_INTEGER || num < Number.MIN_SAFE_INTEGER) {
            console.warn(`${num} is beyond boundary when transfer to integer, the results may not be accurate`);
        }
    }

    /**
     * 精确乘法
     */
    times(num1, num2, ...others) {
        if (others.length > 0) {
            return this.times(this.times(num1, num2), others[0], ...others.slice(1));
        }
        const num1Changed = this.float2Fixed(num1);
        const num2Changed = this.float2Fixed(num2);
        const baseNum = this.digitLength(num1) + this.digitLength(num2);
        const leftValue = num1Changed * num2Changed;

        this.checkBoundary(leftValue);

        return leftValue / Math.pow(10, baseNum);
    }

    /**
     * 精确加法
     */
    plus(num1, num2, ...others) {
        if (others.length > 0) {
            return this.plus(this.plus(num1, num2), others[0], ...others.slice(1));
        }
        const baseNum = Math.pow(10, Math.max(this.digitLength(num1), this.digitLength(num2)));
        return (this.times(num1, baseNum) + this.times(num2, baseNum)) / baseNum;
    }

    /**
     * 精确减法
     */
    minus(num1, num2, ...others) {
        if (others.length > 0) {
            return this.minus(this.minus(num1, num2), others[0], ...others.slice(1));
        }
        const baseNum = Math.pow(10, Math.max(this.digitLength(num1), this.digitLength(num2)));
        return (this.times(num1, baseNum) - this.times(num2, baseNum)) / baseNum;
    }

    /**
     * 精确除法
     */
    floatDiv(num1, num2, ...others) {
        if (others.length > 0) {
            return this.floatDiv(this.floatDiv(num1, num2), others[0], ...others.slice(1));
        }
        const num1Changed = this.float2Fixed(num1);
        const num2Changed = this.float2Fixed(num2);
        this.checkBoundary(num1Changed);
        this.checkBoundary(num2Changed);
        return this.times((num1Changed / num2Changed), Math.pow(10, this.digitLength(num2) - this.digitLength(num1)));
    }

    /**
     * 四舍五入
     */
    round(num, ratio = 0) {
        const base = Math.pow(10, ratio);
        return this.floatDiv(Math.round(this.times(num, base)), base);
    }

	/**
	 * 计算余数
	 */
	floatMod(num1, num2) {
		const num1Changed = this.float2Fixed(num1);
		const num2Changed = this.float2Fixed(num2);
		this.checkBoundary(num1Changed);
		this.checkBoundary(num2Changed);
		return this.minus(num1Changed, this.times(Math.floor(this.floatDiv(num1Changed, num2Changed)), num2Changed));
	}

}

export const numberUtil = new NumberUtil();