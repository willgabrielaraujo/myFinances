import React from "react";
import CurrencyFormatter from "currency-formatter";

export default (props) => {
	const entries = props.entries.map((entry) => {
		return (
			<tr key={entry.id}>
				<td>{entry.description}</td>
				<td>{CurrencyFormatter.format(entry.amount, { locale: "en" })}</td>
				<td>{entry.type}</td>
				<td>{entry.month}</td>
				<td>{entry.status}</td>
				<td>
					<button
						type="button"
						className="btn btn-info"
						title="Set as made"
						disabled={entry.status !== "PENDING"}
						onClick={(e) => props.updateStatus(entry, "MADE")}
					>
						<i className="pi pi-check"></i>
					</button>
					<button
						type="button"
						className="btn btn-warning"
						title="Set as canceled"
						disabled={entry.status !== "PENDING"}
						onClick={(e) => props.updateStatus(entry, "CANCELED")}
					>
						<i className="pi pi-times"></i>
					</button>
					<button
						type="button"
						className="btn btn-primary"
						onClick={(e) => props.editAction(entry.id)}
					>
						<i className="pi pi-pencil"></i>
					</button>
					<button
						type="button"
						className="btn btn-danger"
						onClick={(e) => props.deleteAction(entry)}
					>
						<i className="pi pi-trash"></i>
					</button>
				</td>
			</tr>
		);
	});

	return (
		<table className="table table-hover">
			<thead>
				<tr>
					<th scope="col">Description</th>
					<th scope="col">Amount</th>
					<th scope="col">Type</th>
					<th scope="col">Month</th>
					<th scope="col">Status</th>
					<th scope="col">Actions</th>
				</tr>
			</thead>
			<tbody>{entries}</tbody>
		</table>
	);
};
