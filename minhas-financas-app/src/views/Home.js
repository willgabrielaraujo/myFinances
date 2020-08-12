import React from "react";
import UserService from "../app/services/UserService";
import { AuthContext } from "../main/AuthProvider";

class Home extends React.Component {
	state = {
		balance: 0,
	};

	constructor() {
		super();
		this.UserService = new UserService();
	}

	componentDidMount() {
		const currentUser = this.context.authenticatedUser;

		this.UserService.getBalance(currentUser.id)
			.then((response) => {
				this.setState({ balance: response.data });
			})
			.catch((error) => {
				console.log(error.response);
			});
	}

	render() {
		return (
			<div className="jumbotron">
				<h1 className="display-3">Welcome!</h1>
				<p className="lead">This is your finance application.</p>
				<p className="lead">
					Your balance for the current month is $ {this.state.balance}
				</p>
				<hr className="my-4" />
				<p>Use the menu bar or buttons below to navigate the application.</p>
				<p className="lead">
					<a
						className="btn btn-primary btn-lg"
						href="#/register-user"
						role="button"
					>
						<i className="pi pi-user-plus"></i> Add user
					</a>
					<a className="btn btn-danger btn-lg" href="#/add-entry" role="button">
						<i className="pi pi-plus"></i> Add entry
					</a>
				</p>
			</div>
		);
	}
}

Home.contextType = AuthContext;

export default Home;
