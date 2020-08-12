import React from "react";
import Card from "../components/Card";
import FormGroup from "../components/FormGroup";
import { withRouter } from "react-router-dom";
import UsuarioService from "../app/services/UserService";
import { errorAlert } from "../components/Toastr";
import { AuthContext } from "../main/AuthProvider";

class Login extends React.Component {
	state = {
		email: "",
		password: "",
	};

	constructor() {
		super();
		this.service = new UsuarioService();
	}

	signIn = () => {
		this.service
			.authenticate({
				email: this.state.email,
				password: this.state.password,
			})
			.then((response) => {
				this.context.signIn(response.data);
				this.props.history.push("/home");
			})
			.catch((error) => {
				errorAlert(error.response.data);
			});
	};

	prepareCadastrar = () => {
		this.props.history.push("/register-user");
	};

	render() {
		return (
			<div className="row">
				<div
					className="col-md-6"
					style={{ position: "relative", left: "300px" }}
				>
					<div className="bs-docs-section">
						<Card title="Login">
							<div className="row">
								<div className="col-lg-12">
									<div className="bs-component">
										<fieldset>
											<FormGroup label="Email: *" htmlFor="exampleInputEmail1">
												<input
													type="email"
													value={this.state.email}
													onChange={(e) =>
														this.setState({ email: e.target.value })
													}
													className="form-control"
													id="exampleInputEmail1"
													aria-describedby="emailHelp"
													placeholder="Email"
												/>
											</FormGroup>
											<FormGroup
												label="Password: *"
												htmlFor="exampleInputPassword1"
											>
												<input
													value={this.state.password}
													onChange={(e) =>
														this.setState({ password: e.target.value })
													}
													type="password"
													className="form-control"
													id="exampleInputPassword1"
													placeholder="Password"
												/>
											</FormGroup>
											<button onClick={this.signIn} className="btn btn-success">
												<i className="pi pi-sign-in"></i> Sign in
											</button>
											<button
												className="btn btn-danger"
												onClick={this.prepareCadastrar}
											>
												<i className="pi pi-user-plus"></i> Register
											</button>
										</fieldset>
									</div>
								</div>
							</div>
						</Card>
					</div>
				</div>
			</div>
		);
	}
}

Login.contextType = AuthContext;

export default withRouter(Login);
