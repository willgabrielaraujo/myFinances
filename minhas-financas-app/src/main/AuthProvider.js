import React from "react";
import AuthService from "../app/services/AuthService";

export const AuthContext = React.createContext();
export const AuthConsumer = AuthContext.Consumer;

const AuthenticationProvider = AuthContext.Provider;

class AuthProvider extends React.Component {
	state = {
		authenticatedUser: null,
		isAuthenticated: false,
	};

	signIn = (user) => {
		AuthService.signIn(user);
		this.setState({ isAuthenticated: true, authenticatedUser: user });
	};

	signOut = () => {
		AuthService.removeAuthenticatedUser();
		this.setState({ isAuthenticated: false, authenticatedUser: null });
	};

	render() {
		const context = {
			authenticatedUser: this.state.authenticatedUser,
			isAuthenticated: this.state.isAuthenticated,
			signIn: this.signIn,
			signOut: this.signOut,
		};

		return (
			<AuthenticationProvider value={context}>
				{this.props.children}
			</AuthenticationProvider>
		);
	}
}

export default AuthProvider;
