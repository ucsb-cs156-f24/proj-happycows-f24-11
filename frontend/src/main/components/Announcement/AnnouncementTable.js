import React, { useState } from 'react';
import OurTable, { ButtonColumn } from 'main/components/OurTable';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import { useBackendMutation } from 'main/utils/useBackend';
import {
  cellToAxiosParamsDelete,
  onDeleteSuccess,
} from 'main/utils/announcementUtils';
import { useNavigate } from 'react-router-dom';
import { hasRole } from 'main/utils/currentUser';
import { useParams } from 'react-router-dom';

export default function AnnouncementTable({ announcements, currentUser }) {
  const navigate = useNavigate();
  const { commonsId } = useParams();

  const [showModal, setShowModal] = useState(false);
  const [cellToDelete, setCellToDelete] = useState(null);

  const editCallback = (cell) => {
    navigate(`/announcements/edit/${cell.row.values.id}`);
  };

  // Stryker disable all : hard to test for query caching

  const deleteMutation = useBackendMutation(
    cellToAxiosParamsDelete,
    { onSuccess: onDeleteSuccess },
    [`/api/announcements/getbycommonsid?commonsId=${commonsId}`]
  );
  // Stryker restore all

  // Stryker disable next-line all : TODO try to make a good test for this
  const deleteCallback = async (cell) => {
    setCellToDelete(cell);
    setShowModal(true);
  };

  const confirmDelete = async (cell) => {
    deleteMutation.mutate(cell);
    setShowModal(false);
  };

  const columns = [
    {
      Header: 'id',
      accessor: 'id', // accessor is the "key" in the data
    },
    {
      Header: 'Start Date ISO Format',
      accessor: 'startDate',
    },
    {
      Header: 'End Date ISO Format',
      accessor: 'endDate',
    },
    {
      Header: 'Announcement',
      accessor: 'announcementText',
    },
  ];

  if (hasRole(currentUser, 'ROLE_ADMIN')) {
    columns.push(
      ButtonColumn('Edit', 'primary', editCallback, 'AnnouncementTable')
    );
    columns.push(
      ButtonColumn('Delete', 'danger', deleteCallback, 'AnnouncementTable')
    );
  }

  const announcementsModal = (
    <Modal
      data-testid="AnnouncementsTable-Modal"
      show={showModal}
      onHide={() => setShowModal(false)}
    >
      <Modal.Header closeButton>
        <Modal.Title>Confirm Deletion</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        Are you sure you want to delete this announcement?
      </Modal.Body>
      <Modal.Footer>
        <Button
          variant="secondary"
          data-testid="AnnouncementTable-Modal-Cancel"
          onClick={() => setShowModal(false)}
        >
          Keep this Announcement
        </Button>
        <Button
          variant="danger"
          data-testid="AnnouncementTable-Modal-Delete"
          onClick={() => confirmDelete(cellToDelete)}
        >
          Permanently Delete
        </Button>
      </Modal.Footer>
    </Modal>
  );

  return (
    <>
      <OurTable
        data={announcements}
        columns={columns}
        testid={'AnnouncementTable'}
      />
      {hasRole(currentUser, 'ROLE_ADMIN') && announcementsModal}
    </>
  );
}
