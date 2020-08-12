import React from "react";

import Navbar from "../components/Navbar";

import "toastr/build/toastr.min.js";

import "bootswatch/dist/flatly/bootstrap.css";
import "../custom.css";
import "toastr/build/toastr.css";

import "primereact/resources/themes/nova-light/theme.css";
import "primereact/resources/primereact.min.css";
import "primeicons/primeicons.css";

import Routes from "./Routes";
import AuthProvider from "./AuthProvider";

class App extends React.Component {
	render() {
		return (
			<AuthProvider>
				<Navbar />
				<div className="container">
					<Routes />
				</div>
			</AuthProvider>
		);
	}
}

export default App;
