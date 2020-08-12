import ApiService from "../ApiService";
import ValidationException from "../exception/ValidationException";

class UserService extends ApiService {
  constructor() {
    super("/api/users");
  }

  authenticate(credentials) {
    return this.post("/authenticate", credentials);
  }

  getBalance(id) {
    return this.get(`/${id}/balance`);
  }

  save(user) {
    return this.post("/", user);
  }

  validate(user) {
    const errors = [];

    if (!user.name) {
      errors.push("Name is required");
    }

    if (!user.email) {
      errors.push("Email is required");
    } else if (!user.email.match(/^[a-z0-9.]+@[a-z0-9]+\.[a-z]/)) {
      errors.push("Invalid email");
    }

    if (!user.password) {
      errors.push("Password is required");
    } else if (user.password !== user.passwordConfirm) {
      errors.push("Password confirmation must match password");
    }

    if (errors && errors.length > 0) {
      throw new ValidationException(errors);
    }
  }
}

export default UserService;
