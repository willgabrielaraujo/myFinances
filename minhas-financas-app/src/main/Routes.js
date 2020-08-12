import React from "react";
import { HashRouter, Switch, Route, Redirect } from "react-router-dom";
import Login from "../views/Login";
import Entries from "../views/entries/Entries";
import addEntry from "../views/entries/AddEntry";
import { AuthConsumer } from "./AuthProvider";
import Home from "../views/Home";
import RegisterUser from "../views/RegisterUser";

function AuthRoute({ component: Component, isUserAuthenticated, ...props }) {
	return (
		<Route
			{...props}
			render={(componentProps) => {
				if (isUserAuthenticated) {
					return <Component {...componentProps} />;
				} else {
					return (
						<Redirect
							to={{
								pathname: "/login",
								state: { from: componentProps.location },
							}}
						/>
					);
				}
			}}
		/>
	);
}

function Routes(props) {
	return (
		<HashRouter>
			<Switch>
				<AuthRoute
					isUserAuthenticated={props.isUserAuthenticated}
					path="/"
					exact
					component={Home}
				/>
				<Route path="/register-user" component={RegisterUser} />
				<Route path="/login" component={Login} />
				<AuthRoute
					isUserAuthenticated={props.isUserAuthenticated}
					path="/home"
					component={Home}
				/>
				<AuthRoute
					isUserAuthenticated={props.isUserAuthenticated}
					path="/entries"
					component={Entries}
				/>
				<AuthRoute
					isUserAuthenticated={props.isUserAuthenticated}
					path="/add-entry/:id?"
					component={addEntry}
				/>
			</Switch>
		</HashRouter>
	);
}

export default () => (
	<AuthConsumer>
		{(context) => (
			<Routes isUserAuthenticated={context.isAuthenticated}></Routes>
		)}
	</AuthConsumer>
);
