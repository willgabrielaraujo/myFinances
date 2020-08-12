export default class LocalStorageService {
  static addItem(key, value) {
    localStorage.setItem(key, JSON.stringify(value));
  }

  static getItem(key) {
    const localStorageItem = localStorage.getItem(key);

    return JSON.parse(localStorageItem);
  }

  static deleteItem(key) {
    localStorage.removeItem(key);
  }
}
