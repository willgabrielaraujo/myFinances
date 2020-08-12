import React from "react";
import { withRouter } from "react-router-dom";
import EntryService from "../../app/services/EntryService";
import Card from "../../components/Card";
import FormGroup from "../../components/FormGroup";
import SelectMenu from "../../components/SelectMenu";
import LocalStorageService from "../../app/services/LocalStorageService";
import { successAlert, errorAlert } from "../../components/Toastr";

class AddEntry extends React.Component {
	state = {
		id: null,
		description: "",
		year: "",
		month: "",
		amount: "",
		type: "",
		status: "",
		user: "",
		updating: false,
	};

	constructor() {
		super();
		this.service = new EntryService();
	}

	componentDidMount() {
		const params = this.props.match.params;

		if (params.id) {
			this.service
				.find(params.id)
				.then((response) => {
					this.setState({ ...response.data, updating: true });
				})
				.catch((error) => {
					errorAlert(error.response.data);
				});
		}
	}

	handleChange = (event) => {
		const value = event.target.value;
		const name = event.target.name;

		this.setState({ [name]: value });
	};

	validate = (entry) => {
		try {
			this.service.validate(entry);
		} catch (exception) {
			const messages = exception.messages;
			messages.forEach((message) => errorAlert(message));
			return false;
		}
		return true;
	};

	submit = () => {
		const currencyUser = LocalStorageService.getItem("_current_user");
		const { description, year, month, amount, type } = this.state;
		const entry = {
			description,
			year,
			month,
			amount,
			type,
			user: currencyUser.id,
		};

		if (!this.validate(entry)) {
			return false;
		}

		this.service
			.save(entry)
			.then((response) => {
				successAlert("Entry added.");
				this.goToEntries();
			})
			.catch((error) => {
				errorAlert(error.response.data);
			});
	};

	update = () => {
		const {
			description,
			year,
			month,
			amount,
			type,
			status,
			user,
			id,
		} = this.state;
		const entry = {
			description,
			year,
			month,
			amount,
			type,
			status,
			user,
			id,
		};

		if (!this.validate(entry)) {
			return false;
		}

		this.service
			.update(entry)
			.then((response) => {
				successAlert("Entry has updated");
				this.props.history.push("/entries");
			})
			.catch((error) => {
				errorAlert(error.response.data);
			});
	};

	goToEntries = () => {
		this.props.history.push("/entries");
	};

	render() {
		const months = this.service.getMonthList();
		const types = this.service.getTypeList();

		return (
			<Card title={this.state.updating ? "Update Entry" : "Add Entry"}>
				<div className="row">
					<div className="col-md-12">
						<FormGroup id="inputDescription" label="Description: *">
							<input
								id="inputDescription"
								type="text"
								className="form-control"
								name="description"
								value={this.state.description}
								onChange={this.handleChange}
							/>
						</FormGroup>
					</div>
				</div>
				<div className="row">
					<div className="col-md-6">
						<FormGroup id="inputYear" label="Year: *">
							<input
								id="inputYear"
								type="text"
								name="year"
								value={this.state.year}
								onChange={this.handleChange}
								className="form-control"
							/>
						</FormGroup>
					</div>
					<div className="col-md-6">
						<FormGroup id="inputMonth" label="Month: *">
							<SelectMenu
								id="inputMonth"
								options={months}
								name="month"
								value={this.state.month}
								onChange={this.handleChange}
								className="form-control"
							/>
						</FormGroup>
					</div>
				</div>
				<div className="row">
					<div className="col-md-4">
						<FormGroup id="inputValue" label="Value: *">
							<input
								id="inputValue"
								type="text"
								name="amount"
								value={this.state.amount}
								onChange={this.handleChange}
								className="form-control"
							/>
						</FormGroup>
					</div>
					<div className="col-md-4">
						<FormGroup id="inputType" label="Type: *">
							<SelectMenu
								id="inputType"
								options={types}
								name="type"
								value={this.state.type}
								onChange={this.handleChange}
								className="form-control"
							/>
						</FormGroup>
					</div>

					<div className="col-md-4">
						<FormGroup id="inputStatus" label="Status: ">
							<input
								type="text"
								className="form-control"
								name="status"
								value={this.state.status}
								disabled
							/>
						</FormGroup>
					</div>
				</div>
				<div className="row">
					<div className="col-md-6">
						{this.state.updating ? (
							<button className="btn btn-primary" onClick={this.update}>
								<i className="pi pi-refresh"></i> Update
							</button>
						) : (
							<button className="btn btn-success" onClick={this.submit}>
								<i className="pi pi-save"></i> Save
							</button>
						)}
						<button className="btn btn-danger" onClick={this.goToEntries}>
							<i className="pi pi-times"></i> Cancel
						</button>
					</div>
				</div>
			</Card>
		);
	}
}

export default withRouter(AddEntry);
