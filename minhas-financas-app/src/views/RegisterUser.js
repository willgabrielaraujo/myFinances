import React from "react";
import Card from "../components/Card";
import FormGroup from "../components/FormGroup";
import { withRouter } from "react-router-dom";
import UsuarioService from "../app/services/UserService";
import { errorAlert, successAlert } from "../components/Toastr";

class RegisterUser extends React.Component {
	state = {
		name: "",
		email: "",
		password: "",
		passwordConfirm: "",
	};

	constructor() {
		super();
		this.service = new UsuarioService();
	}

	validate = (user) => {
		try {
			this.service.validate(user);
		} catch (exception) {
			const messages = exception.messages;
			messages.forEach((message) => errorAlert(message));
			return false;
		}
		return true;
	};

	save = () => {
		const { name, email, password, passwordConfirm } = this.state;
		const user = { name, email, password, passwordConfirm };

		if (!this.validate(user)) {
			return false;
		}

		this.service
			.save(user)
			.then((response) => {
				successAlert("The user was added! Sign in to access your profile.");
				this.props.history.push("/login");
			})
			.catch((error) => {
				errorAlert(error.response.data);
			});
	};

	cancel = () => {
		this.props.history.push("/login");
	};

	render() {
		return (
			<Card title="Resgister User">
				<div className="row">
					<div className="col-lg-12">
						<fieldset>
							<FormGroup label="Name: *" htmlFor="exampleInputName">
								<input
									type="email"
									value={this.state.name}
									onChange={(e) => this.setState({ name: e.target.value })}
									className="form-control"
									id="exampleInputName"
									aria-describedby="emailHelp"
									placeholder="Name"
								/>
							</FormGroup>
							<FormGroup label="Email: *" htmlFor="exampleInputEmail1">
								<input
									type="email"
									value={this.state.email}
									onChange={(e) => this.setState({ email: e.target.value })}
									className="form-control"
									id="exampleInputEmail1"
									aria-describedby="emailHelp"
									placeholder="Email"
								/>
								<small id="emailHelp" className="form-text text-muted">
									We don't share your email.
								</small>
							</FormGroup>
							<FormGroup label="password: *" htmlFor="exampleInputPassword1">
								<input
									type="password"
									value={this.state.password}
									onChange={(e) => this.setState({ password: e.target.value })}
									className="form-control"
									id="exampleInputPassword1"
									placeholder="Password"
								/>
							</FormGroup>
							<FormGroup
								label="Password Confirm: *"
								htmlFor="exampleInputPassword2"
							>
								<input
									type="password"
									value={this.state.passwordConfirm}
									onChange={(e) =>
										this.setState({ passwordConfirm: e.target.value })
									}
									className="form-control"
									id="exampleInputPassword2"
									placeholder="Password Confirm"
								/>
							</FormGroup>
							<button
								type="button"
								className="btn btn-success"
								onClick={this.save}
							>
								<i className="pi pi-save"></i> Save
							</button>
							<button
								type="button"
								className="btn btn-danger"
								onClick={this.cancel}
							>
								<i className="pi pi-minus"></i> Cancel
							</button>
						</fieldset>
					</div>
				</div>
			</Card>
		);
	}
}
export default withRouter(RegisterUser);
