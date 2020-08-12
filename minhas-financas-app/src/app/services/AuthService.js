import LocalStorageService from "./LocalStorageService";

export const CURRENT_USER = "_current_user";

export default class AuthService {
	static isUserAuthenticated() {
		const currentyUser = LocalStorageService.getItem(CURRENT_USER);
		return currentyUser && currentyUser.id;
	}

	static removeAuthenticatedUser() {
		LocalStorageService.deleteItem(CURRENT_USER);
	}

	static signIn(user) {
		LocalStorageService.addItem(CURRENT_USER, user);
	}

	static getAuthenticatedUser() {
		return LocalStorageService.getItem(CURRENT_USER);
	}
}
