import React from 'react'

export default (props) => {
  const options = props.options.map((option, key) => {
    return (
      <option key={key} value={option.value}>{option.label}</option>
    )
  })

  return (
    <select {...props}>
      {options}
    </select>
  )
}