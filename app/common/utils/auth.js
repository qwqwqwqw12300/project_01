const TokenKey = 'TOKENKEY'

export const setToken = (token) => {
	return uni.setStorageSync(TokenKey, token);
};
export const getToken = () => {
	return uni.getStorageSync(TokenKey);
};
export const removeToken = () => {
	return uni.removeStorageSync(TokenKey);
}
