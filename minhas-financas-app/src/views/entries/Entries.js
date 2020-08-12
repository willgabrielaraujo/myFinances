import React from "react";
import { withRouter } from "react-router-dom";
import Card from "../../components/Card";
import FormGroup from "../../components/FormGroup";
import SelectMenu from "../../components/SelectMenu";
import EntryList from "./EntryList";
import EntryService from "../../app/services/EntryService";
import LocalStorageService from "../../app/services/LocalStorageService";
import {
	errorAlert,
	successAlert,
	warningAlert,
} from "../../components/Toastr";
import { Dialog } from "primereact/dialog";
import { Button } from "primereact/button";

class Entries extends React.Component {
	state = {
		year: "",
		month: "",
		description: "",
		type: "",
		status: "",
		showDeletionConfirmDialog: false,
		entryToBeDeleted: {},
		entries: [],
	};

	constructor() {
		super();
		this.service = new EntryService();
	}

	validate = () => {
		if (!this.state.year) {
			errorAlert("Year is required");
			return false;
		}

		return true;
	};

	search = () => {
		if (!this.validate()) {
			return false;
		}

		const currentUser = LocalStorageService.getItem("_current_user");

		this.service
			.filter({
				user: currentUser.id,
				year: this.state.year,
				month: this.state.month,
				description: this.state.description,
				type: this.state.type,
				status: this.state.status,
			})
			.then((response) => {
				const entries = response.data;

				if (entries.length > 0) {
					successAlert("Data fetched!");
					this.setState({ entries });
				} else {
					warningAlert("Oops! No data found.");
					this.setState({ entries: [] });
				}
			})
			.catch((error) => {
				errorAlert(error);
			});
	};

	editEntry = (id) => {
		this.props.history.push(`/add-entry/${id}`);
	};

	deleteEntry = () => {
		this.service
			.deleteEntry(this.state.entryToBeDeleted.id)
			.then((response) => {
				this.removeFromEntryList(this.state.entryToBeDeleted);
				this.hideDeletionDialog();
				successAlert("Entry deleted!");
			})
			.catch((error) => {
				errorAlert("Failed to delete entry!");
			});
	};

	removeFromEntryList(entry) {
		const entries = this.state.entries;
		const entryIndex = entries.indexOf(entry);
		entries.splice(entryIndex, 1);
		this.setState(entries);
	}

	showDeletionDialog = (entry) => {
		this.setState({ showDeletionConfirmDialog: true, entryToBeDeleted: entry });
	};

	hideDeletionDialog = () => {
		this.setState({ showDeletionConfirmDialog: false, entryToBeDeleted: {} });
	};

	addEntry = () => {
		this.props.history.push("/add-entry");
	};

	changeStatus = (entry, status) => {
		this.service
			.updateStatus(entry.id, status)
			.then((response) => {
				const entries = this.state.entries;
				const index = entries.indexOf(entry);

				if (index !== -1) {
					entry["status"] = status;
					entries[index] = entry;
					this.setState({ entry });
				}

				successAlert("Entry status updated");
			})
			.catch((error) => {
				errorAlert(error.response.data);
			});
	};

	render() {
		const months = this.service.getMonthList();
		const types = this.service.getTypeList();
		const status = this.service.getStatusList();

		const deletionDialogFooter = (
			<div>
				<Button label="Yes" icon="pi pi-check" onClick={this.deleteEntry} />
				<Button
					label="No"
					icon="pi pi-times"
					onClick={this.hideDeletionDialog}
					className="p-button-secondary"
				/>
			</div>
		);
		return (
			<Card title="Search Entries">
				<div className="row">
					<div className="col-lg-6">
						<div className="bs-component">
							<fieldset>
								<FormGroup label="Ano: *" htmlFor="inputYear">
									<input
										type="text"
										className="form-control"
										value={this.state.year}
										onChange={(e) => this.setState({ year: e.target.value })}
										id="inputYear"
										placeholder="Year"
									/>
								</FormGroup>
								<FormGroup label="Month: " htmlFor="inputMonth">
									<SelectMenu
										id="inputMonth"
										value={this.state.month}
										onChange={(e) => this.setState({ month: e.target.value })}
										className="form-control"
										options={months}
									/>
								</FormGroup>
								<FormGroup label="Description: *" htmlFor="inputDescription">
									<input
										type="text"
										className="form-control"
										value={this.state.description}
										onChange={(e) =>
											this.setState({ description: e.target.value })
										}
										id="inputDescription"
										placeholder="Description"
									/>
								</FormGroup>
								<FormGroup label="Type: " htmlFor="inputType">
									<SelectMenu
										id="inputType"
										value={this.state.type}
										onChange={(e) => this.setState({ type: e.target.value })}
										className="form-control"
										options={types}
									/>
								</FormGroup>
								<FormGroup label="Status: " htmlFor="inputStatus">
									<SelectMenu
										id="inputStatus"
										value={this.state.status}
										onChange={(e) => this.setState({ status: e.target.value })}
										className="form-control"
										options={status}
									/>
								</FormGroup>
								<button
									type="button"
									className="btn btn-primary"
									onClick={this.search}
								>
									<i className="pi pi-search"></i> Search
								</button>
								<button
									type="button"
									className="btn btn-success"
									onClick={this.addEntry}
								>
									<i className="pi pi-plus"></i> Add
								</button>
							</fieldset>
						</div>
					</div>
				</div>
				<br />
				<div className="row">
					<div className="col-lg-12">
						<EntryList
							entries={this.state.entries}
							editAction={this.editEntry}
							deleteAction={this.showDeletionDialog}
							updateStatus={this.changeStatus}
						/>
					</div>
				</div>
				<div>
					<Dialog
						header="Delete entry"
						visible={this.state.showDeletionConfirmDialog}
						style={{ width: "50vw" }}
						modal={true}
						footer={deletionDialogFooter}
						onHide={() => this.setState({ showDeletionConfirmDialog: false })}
					>
						Do you confirm the deletion of this entry?
					</Dialog>
				</div>
			</Card>
		);
	}
}

export default withRouter(Entries);
